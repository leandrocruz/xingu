package xingu.node.commons.signal;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public interface ReverseSignalHandler
{
	Signal query(Signal signal, long queryTimeout)
			throws Exception;

	Signal query(Signal signal, ChannelFutureListener onWrite, long queryTimeout)
		throws Exception;

	ChannelFuture deliver(Signal signal)
		throws Exception;
}