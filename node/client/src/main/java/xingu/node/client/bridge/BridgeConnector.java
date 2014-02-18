package xingu.node.client.bridge;

import org.jboss.netty.channel.Channel;

public interface BridgeConnector
{
	Channel connect();
	
	Channel getAcceptedChannel();
}
