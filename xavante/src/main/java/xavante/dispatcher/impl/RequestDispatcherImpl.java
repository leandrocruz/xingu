package xavante.dispatcher.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import xavante.Xavante;
import xavante.XavanteRequest;
import xavante.XavanteRequestFactory;
import xavante.dispatcher.DefaultRequestHandler;
import xavante.dispatcher.RequestDispatcher;
import xavante.dispatcher.RequestHandler;
import xingu.url.Url;
import xingu.url.UrlParser;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class RequestDispatcherImpl
	implements RequestDispatcher, Configurable
{
	@Inject
	private Factory						factory;

	private Entry[]		handlers;

	private static final RequestHandler	deflt	= new DefaultRequestHandler();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration[] handlers = conf.getChild("handlers").getChildren("handler");
		this.handlers = new Entry[handlers.length];
		
		int i = 0;
		for(Configuration h : handlers)
		{
			String        path          = h.getAttribute("path");
			String        className     = h.getAttribute("class");
			Configuration handlerConfig = h.getChild("conf");
			Class<?>       clazz     = ObjectUtils.loadClass(className);
			RequestHandler handler   = (RequestHandler) factory.create(clazz, handlerConfig, path);
			this.handlers[i++] = new Entry(path, handler);
		}
	}

	@Override
	public void dispatch(HttpRequest req, Channel channel)
		throws Exception
	{
		String uri  = req.getUri();
		Url    url  = UrlParser.parse(uri);
		String path = url.getPath();

		RequestHandler handler = byPath(path);

		String fixed = fixHttp1_0AndRemovedHandlerPath(url, handler);
		if(!uri.equals(fixed))
		{
			url = UrlParser.parse(fixed);
			req.setUri(fixed);
		}

		XavanteRequest xeq = XavanteRequestFactory.build(req, channel, url);
		handler.handle(xeq);
	}

	private String fixHttp1_0AndRemovedHandlerPath(Url url, RequestHandler handler)
	{
		/*
		 * Fixes badly formed HTTP 1.0 requests, also
		 * normalizes the path using StringUtils.normalizedPath()
		 */
		String fixed = url.getPathQueryStringAndFragment();

		/*
		 * Removes the context/handler path from the original path
		 */
		String  handlerPath = handler.getConfiguredPath();
		if(handlerPath != null /* !DefaultRequestHandler */)
		{
			boolean root = Xavante.isRoot(handlerPath);
			if(!root)
			{
				int len = handlerPath.length();
				fixed = fixed.substring(len);
				if(StringUtils.EMPTY.equals(fixed))
				{
					fixed = Xavante.SLASH;
				}
			}
			
		}
		
		if(!fixed.startsWith(Xavante.SLASH))
		{
			fixed = Xavante.SLASH + fixed;
		}
		
		return fixed;
	}

	@Override
	public RequestHandler byPath(String path)
	{
		for(Entry entry : handlers)
		{
			if(path.startsWith(entry.path))
			{
				return entry.handler;
			}
		}

		return deflt;
	}
}

class Entry
{
	String path;
	
	RequestHandler handler;
	
	public Entry(String path, RequestHandler handler)
	{
		this.path    = path;
		this.handler = handler;
	}
}