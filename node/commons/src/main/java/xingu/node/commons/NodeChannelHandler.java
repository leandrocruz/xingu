package xingu.node.commons;

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.lang.NotImplementedYet;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.SignalHandler;
import xingu.node.commons.signal.impl.ChannelDisconnected;
import xingu.node.commons.signal.impl.ExceptionCaught;
import xingu.node.commons.signal.impl.NotASignal;

public class NodeChannelHandler
	extends SimpleChannelHandler
{
	@Inject
	private SignalHandler	bridge;

	private Logger			logger	= LoggerFactory.getLogger(getClass());
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		bridge.on(new ChannelDisconnected(channel), channel);
		ctx.sendUpstream(e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		Throwable cause = e.getCause();
		bridge.on(new ExceptionCaught(channel, cause), channel);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel       channel  = e.getChannel();
		SocketAddress addr     = channel.getRemoteAddress();
		Object        msg      = e.getMessage();
		boolean       isSignal = msg instanceof Signal;
		String        name     = msg.getClass().getName();
		if(isSignal)
		{
			logger.debug("Processing signal '{}' from {}", name, addr);
			Signal signal = (Signal) msg;
			bridge.on(signal, channel);
		}
		else
		{
			logger.warn("Message '{}' from {} is not a signal", name, addr);
			bridge.on(new NotASignal(msg), channel);
		}
	}
}