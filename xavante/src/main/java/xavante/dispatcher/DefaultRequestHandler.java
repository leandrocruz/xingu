package xavante.dispatcher;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import xavante.dispatcher.impl.RequestHandlerSupport;
import xingu.netty.http.HttpResponseBuilder;

public class DefaultRequestHandler
	extends RequestHandlerSupport
{
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss Z");
	@Override
	public void handle(HttpRequest req, Channel channel)
		throws Exception
	{
		String uri  = req.getUri();
		String host = req.getHeader(HttpHeaders.Names.HOST);
		String conn = req.getHeader(HttpHeaders.Names.CONNECTION);
		HttpResponse res = HttpResponseBuilder
				.builder(HttpResponseStatus.OK)
				.withHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8")
				.withHeader(HttpHeaders.Names.SERVER, "Xavante 1.0")
				.withHeader(HttpHeaders.Names.DATE, df.format(new Date()))
				.withContent("URL: http://" + host + uri)
				.build();

		ChannelFuture future = channel.write(res);
		if(HttpHeaders.Values.CLOSE.equalsIgnoreCase(conn))
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public String getPath()
	{
		return "/";
	}
}