package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;

public class ChannelDisconnected<T>
	extends SignalSupport<T>
{
	private Channel	channel;

	public ChannelDisconnected(Channel channel)
	{
		this.channel = channel;
	}

	public Channel getChannel(){return channel;}
	public void setChannel(Channel channel){this.channel = channel;}
}
