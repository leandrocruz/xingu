package xingu.node.commons.signal;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public interface ReverseSignalHandler
{
	Signal query(Signal signal, ChannelFutureListener onWrite)
		throws Exception;

	ChannelFuture deliver(Signal signal)
		throws Exception;
}