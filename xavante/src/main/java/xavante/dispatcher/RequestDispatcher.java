package xavante.dispatcher;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

public interface RequestDispatcher
{
	RequestHandler byPath(String path);
	
	void dispatch(HttpRequest req, Channel channel)
		throws Exception;
}
