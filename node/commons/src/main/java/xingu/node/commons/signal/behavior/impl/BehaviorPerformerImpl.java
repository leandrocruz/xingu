package xingu.node.commons.signal.behavior.impl;

import org.jboss.netty.channel.Channel;

import xingu.container.Inject;
import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.BehaviorPerformer;
import xingu.node.commons.signal.behavior.BehaviorResolver;
import xingu.node.commons.signal.behavior.SignalBehavior;

public class BehaviorPerformerImpl
	implements BehaviorPerformer
{
	@Inject
	private BehaviorResolver resolver;

	@Override
	public Signal<?> performBehavior(Signal<?> signal, Channel channel)
		throws Exception
	{
		SignalBehavior<Signal<?>> behavior = resolver.behaviorFor(signal);
		Signal<?> reply = behavior.perform(signal, channel);
		return reply;
	}
}
