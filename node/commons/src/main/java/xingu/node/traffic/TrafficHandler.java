package xingu.node.traffic;

import java.util.Collection;

import org.jboss.netty.channel.Channel;

public interface TrafficHandler
{
	void onConnect(Channel channel);
	
	void onDisconnect(Channel channel);

	void onWrite(Channel channel);

	void onMessage(Channel channel);

	Collection<ChannelData> getData();

	ChannelData byChannel(int id);
}
