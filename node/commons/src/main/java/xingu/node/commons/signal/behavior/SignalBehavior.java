package xingu.node.commons.signal.behavior;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;

public interface SignalBehavior<T extends Signal<?>>
{
	Signal<?> perform(T signal, Channel channel)
		throws Exception;
}
