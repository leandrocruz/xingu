package xavante.dispatcher.impl;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

import xavante.dispatcher.RequestHandler;

public class RequestHandlerSupport
	implements RequestHandler
{
	@Override
	public void handle(HttpRequest req, Channel channel)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
