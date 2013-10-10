package xavante.dispatcher.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

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
	private Factory					factory;

	private RequestHandler			deflt		= new DefaultRequestHandler();

	private List<RequestHandler>	handlers	= new ArrayList<RequestHandler>();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		handlers.add(factory.create(LongPollingCometHandler.class));
	}

	@Override
	public void dispatch(HttpRequest req, Channel channel)
		throws Exception
	{
		RequestHandler handler = handlerFor(req);
		fixPath(req, handler);
		handler.handle(req, channel);
	}

	private void fixPath(HttpRequest req, RequestHandler handler)
	{
		String         path    = handler.getPath();
		String         uri     = req.getUri();
		if(!uri.startsWith(path))
		{
			throw new NotImplementedYet();
		}

		int len = path.length();
		uri     = uri.substring(len);
		req.setUri(uri);
	}

	private RequestHandler handlerFor(HttpRequest req)
	{
		for(RequestHandler handler : handlers)
		{
			boolean accepts = handler.accepts(req);
			if(accepts)
			{
				return handler;
			}
		}
		return deflt;
	}
}