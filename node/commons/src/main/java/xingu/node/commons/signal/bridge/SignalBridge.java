package xingu.node.commons.signal.bridge;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.node.commons.signal.Signal;

public interface SignalBridge
{
	void on(Channel channel, Signal signal)
		throws Exception;

	Signal query(Channel channel, ChannelFutureListener onWrite, Signal signal)
		throws Exception;

	ChannelFuture deliver(Channel channel, Signal signal)
		throws Exception;
}