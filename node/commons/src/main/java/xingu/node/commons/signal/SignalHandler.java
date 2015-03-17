package xingu.node.commons.signal;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.node.commons.signal.Signal;

public interface SignalHandler
{
	void on(Signal signal, Channel channel)
		throws Exception;

	Signal query(Signal signal, ChannelFutureListener onWrite, Channel channel, long queryTimeoput)
		throws Exception;

	ChannelFuture deliver(Signal signal, Channel channel)
		throws Exception;
}