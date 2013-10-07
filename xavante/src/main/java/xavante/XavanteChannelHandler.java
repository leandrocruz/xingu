package xavante;

import java.net.SocketAddress;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

import xavante.dispatcher.RequestDispatcher;

import xingu.netty.http.HttpResponseBuilder;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;

public class XavanteChannelHandler
	extends SimpleChannelHandler
{
	@Inject
	private RequestDispatcher	dispatcher;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel       channel = e.getChannel();
		SocketAddress addr    = channel.getRemoteAddress();
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel       channel = e.getChannel();
		SocketAddress addr    = channel.getRemoteAddress();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		e.getCause().printStackTrace();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		long start = System.currentTimeMillis();
		doWork(e);
		long end   = System.currentTimeMillis();
		System.err.println("XavanteChannelHandler done in " + (end - start) + " ms");
	}

	private void doWork(MessageEvent e)
		throws Exception
	{
		Channel channel     = e.getChannel();
		Object  msg         = e.getMessage();
		boolean isWebsocket = isWebSocketUpgrade(msg);

		if(isWebsocket)
		{
			throw new NotImplementedYet();
		}
		else if(msg instanceof HttpRequest)
		{
			HttpRequest req = HttpRequest.class.cast(msg);
			handle(req, channel);
		}
		else if(msg instanceof WebSocketFrame)
		{
			throw new NotImplementedYet();
		}
	}

	private void handle(HttpRequest req, Channel channel)
	{
		try
		{
			dispatcher.dispatch(req, channel);
		}
		catch(Throwable t)
		{
			HttpResponse res = toInternalServerError(t);
			channel.write(res).addListener(ChannelFutureListener.CLOSE);
		}
	}

	private HttpResponse toInternalServerError(Throwable t)
	{
		String       trace = ExceptionUtils.getStackTrace(t);
		StringBuffer sb    = new StringBuffer("Internal Server Error: <br/>");
		sb.append("\n");
		sb.append(trace);
		return HttpResponseBuilder
				.builder(HttpResponseStatus.INTERNAL_SERVER_ERROR)
				.withContent(sb.toString())
				.build();
	}

	public static boolean isWebSocketUpgrade(Object msg)
	{
		if(msg instanceof HttpRequest == false)
		{
			return false;
		}

		HttpRequest res        = HttpRequest.class.cast(msg);
		String      connection = res.getHeader(HttpHeaders.Names.CONNECTION);
		String      upgrade    = res.getHeader(HttpHeaders.Names.UPGRADE);
		return HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(upgrade) && HttpHeaders.Values.UPGRADE.equalsIgnoreCase(connection);
	}
}