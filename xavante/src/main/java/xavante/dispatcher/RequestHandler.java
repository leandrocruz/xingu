package xavante.dispatcher;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

import xavante.XavanteRequest;
import xingu.url.Url;


public interface RequestHandler
{
	void handle(XavanteRequest req)
		throws Exception;
}
