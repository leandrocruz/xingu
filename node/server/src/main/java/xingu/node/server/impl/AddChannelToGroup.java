package xingu.node.server.impl;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;

public class AddChannelToGroup
	extends SimpleChannelUpstreamHandler
{
	private final ChannelGroup group;
	
	public AddChannelToGroup(ChannelGroup group)
	{
		this.group = group;
	}
	
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		Channel channel = e.getChannel();
		group.add(channel);
	}
}