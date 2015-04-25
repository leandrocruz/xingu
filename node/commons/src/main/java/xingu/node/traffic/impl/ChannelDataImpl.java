package xingu.node.traffic.impl;

import org.jboss.netty.channel.Channel;

import xingu.node.traffic.ChannelData;

public class ChannelDataImpl
	implements ChannelData
{

	private Channel	channel;

	private long	created;

	private long	last;

	public ChannelDataImpl(Channel channel, long now)
	{
		this.channel = channel;
		this.created = now;
		this.last = now;
		
	}

	@Override
	public long timeForLastEvent()
	{
		return last;
	}

	@Override
	public Channel getChannel()
	{
		return channel;
	}

	public void setLast(long time)
	{
		this.last = time;
	}

}
