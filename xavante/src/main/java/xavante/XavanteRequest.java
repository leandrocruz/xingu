package xavante;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import xingu.url.Url;

public interface XavanteRequest
{
	Channel getChannel();
	
	HttpRequest getRequest();
	
	Url getUrl();

	ChannelFuture write(HttpResponse response)
		throws Exception;

	String getPath();
}
