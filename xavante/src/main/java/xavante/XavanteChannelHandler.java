package xavante;

import java.net.SocketAddress;
import java.net.SocketException;

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

import xavante.dispatcher.RequestDispatcher;
import xingu.netty.http.HttpResponseBuilder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

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
		Xavante.logger.info("XavanteChannelHandler {} connected", addr);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel       channel = e.getChannel();
		SocketAddress addr    = channel.getRemoteAddress();
		Xavante.logger.info("XavanteChannelHandler {} disconnected", addr);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		Throwable cause = e.getCause();
		if(cause instanceof SocketException)
		{
			//we get a 'Broken pipe' when the client closes the connection from his end
			Xavante.logger.info("XavanteChannelHandlerexceptionCaught(): {}", cause.getMessage());
		}
		else
		{
			String trace = ExceptionUtils.getStackTrace(cause);
			Xavante.logger.info("XavanteChannelHandlerexceptionCaught(): {}", trace);
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		long start = System.currentTimeMillis();
		doWork(e);
		long end   = System.currentTimeMillis();
		Xavante.logger.info("XavanteChannelHandler done in {} ms", end - start);
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
			Xavante.logger.error("Error handling dispatch: " + t.getMessage(), t);
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