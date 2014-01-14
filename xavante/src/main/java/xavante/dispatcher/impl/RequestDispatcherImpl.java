package xavante.dispatcher.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import xavante.Xavante;
import xavante.dispatcher.DefaultRequestHandler;
import xavante.dispatcher.RequestDispatcher;
import xavante.dispatcher.RequestHandler;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class RequestDispatcherImpl
	implements RequestDispatcher, Configurable
{
	@Inject
	private Factory						 factory;

	private RequestHandler				 deflt			= new DefaultRequestHandler();

	private Map<String, RequestHandler>	 handlerByPath	= new HashMap<String, RequestHandler>();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration[] handlers = conf.getChild("handlers").getChildren("handler");
		for(Configuration h : handlers)
		{
			String         path    = h.getAttribute("path");
			String         clazz   = h.getAttribute("class");
			RequestHandler handler = (RequestHandler) factory.create(clazz);
			handlerByPath.put(path, handler);
		}
	}

	@Override
	public void dispatch(HttpRequest req, Channel channel)
		throws Exception
	{
		RequestHandler handler = deflt;
		String         path    = Xavante.SLASH;
		String         uri     = req.getUri();
		for(String key : handlerByPath.keySet())
		{
			if(uri.startsWith(key))
			{
				handler = handlerByPath.get(key);
				path    = key;
				break;
			}
		}

		fixPath(req, path);
		handler.handle(req, channel);
	}

	private void fixPath(HttpRequest req, String path)
	{
		String uri = req.getUri();
		if(!uri.startsWith(path))
		{
			throw new NotImplementedYet();
		}

		int len = path.length();
		uri     = uri.substring(len);
		if(StringUtils.EMPTY.equals(uri))
		{
			uri = Xavante.SLASH;
		}
		req.setUri(uri);
	}
}