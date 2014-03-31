package xingu.node.client.bridge;

import java.util.concurrent.Future;

import org.jboss.netty.channel.Channel;

public interface BridgeConnector
{
	Future<Channel> connect(OnConnect onConnect);
	
	Channel getAcceptedChannel();
}
