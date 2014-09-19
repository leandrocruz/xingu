package xingu.node.commons.signal.behavior;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;

public interface BehaviorPerformer
{
	Signal<?> performBehavior(Signal<?> signal, Channel channel)
		throws Exception;
}
