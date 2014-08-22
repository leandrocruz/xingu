package xavante.dispatcher.impl;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

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
			String         path      = h.getAttribute("path");
			String         className = h.getAttribute("class");
			Class<?>       clazz     = ObjectUtils.loadClass(className);
			RequestHandler handler   = (RequestHandler) factory.create(clazz);
			this.handlers[i++] = new Entry(path, handler);
		}
	}

	@Override
	public void dispatch(HttpRequest req, Channel channel)
		throws Exception
	{
		RequestHandler handler = deflt;
		String         uri     = req.getUri();
		Url            url     = UrlParser.parse(uri);
		String         path    = url.getPath();

		for(Entry entry : handlers)
		{
			if(path.startsWith(entry.path))
			{
				handler = entry.handler;
				break;
			}
		}

		/*
		 * Fixes badly formed HTTP 1.0 requests, also
		 * normalizes the path
		 */
		req.setUri(path);
		
		XavanteRequest xeq = XavanteRequestFactory.build(req, channel, url);
		handler.handle(xeq);
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