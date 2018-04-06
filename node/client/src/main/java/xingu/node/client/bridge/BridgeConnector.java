package xingu.node.client.bridge;

import org.jboss.netty.channel.Channel;

public interface BridgeConnector
{
	void connect(String host, int[] ports, OnConnect onConnect);
	
	Channel getAcceptedChannel();
}
