package xavante.comet;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface MessageFactory
{
	CometMessage build(HttpRequest req, HttpResponse resp, Channel channel)
		throws Exception;

	int getMessageIdLength();
}
