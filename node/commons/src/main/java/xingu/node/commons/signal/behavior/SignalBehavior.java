package xingu.node.commons.signal.behavior;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;

public interface SignalBehavior<T extends Signal, S extends Signal>
{
	S perform(T signal, Channel channel)
		throws Exception;
}
