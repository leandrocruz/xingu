package xingu.node.commons.signal.bridge;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import xingu.node.commons.signal.Signal;

public interface SignalBridge
{
	void on(Channel channel, Signal signal)
		throws Exception;

	Signal query(Channel channel, Signal signal)
		throws Exception;

	ChannelFuture deliver(Channel channel, Signal signal)
		throws Exception;
}