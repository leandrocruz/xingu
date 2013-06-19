package xingu.tunnel.impl;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import xingu.tunnel.redirector.Redirector;
import br.com.ibnetwork.xingu.container.Inject;

public class AgentAcceptorChannelHandler
	extends SimpleChannelHandler
{
	@Inject
	private Redirector redirector;
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		redirector.onAgentConnected(channel);
		channel.write("OK");
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		redirector.onAgentDisconnected(channel);
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
		ChannelBuffer buffer  = (ChannelBuffer) e.getMessage();
		Channel       channel = e.getChannel();
		redirector.onAgentMessage(channel, buffer);
	}
}