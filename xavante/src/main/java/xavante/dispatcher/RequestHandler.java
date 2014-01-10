package xavante.dispatcher;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;


public interface RequestHandler
{
	void handle(HttpRequest req, Channel channel)
		throws Exception;
}
