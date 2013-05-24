package xingu.netty.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class SayHi
	extends SimpleChannelHandler
{
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		System.out.println("[SAY HI] Channel Connected: "
				+ channel.getLocalAddress() + " <-> " + channel.getRemoteAddress());
		channel.write("Hi " + System.currentTimeMillis());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		System.out.println("[SAY HI] Channel Disconnected: "
				+ channel.getLocalAddress() + " <-> " + channel.getRemoteAddress());
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
		throws Exception
	{
		Channel channel = e.getChannel();
		Object  msg     = e.getMessage();
		String  s       = (String) msg;
		System.out.println("[SAY HI] Message Received: "
				+ channel.getLocalAddress() + " <-> " + channel.getRemoteAddress()
				+ " -- " 
				+ s);
		channel.write(s + " " + System.currentTimeMillis());
	}
}