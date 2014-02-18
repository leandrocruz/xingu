package xingu.node.commons.signal.bridge;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import xingu.node.commons.signal.Signal;

public interface ReverseBridge
{
	Signal query(Signal signal, ChannelFutureListener onWrite)
		throws Exception;

	ChannelFuture deliver(Signal signal)
		throws Exception;

}
