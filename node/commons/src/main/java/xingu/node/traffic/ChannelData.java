package xingu.node.traffic;

import org.jboss.netty.channel.Channel;

public interface ChannelData
{
	long timeForLastEvent();

	Channel getChannel();

	void addPing(long duration);
}
