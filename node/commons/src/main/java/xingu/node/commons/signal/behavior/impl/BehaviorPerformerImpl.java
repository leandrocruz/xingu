package xingu.node.commons.signal.behavior.impl;

import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.BehaviorPerformer;
import xingu.node.commons.signal.behavior.BehaviorResolver;
import xingu.node.commons.signal.behavior.SignalBehavior;
import br.com.ibnetwork.xingu.container.Inject;

public class BehaviorPerformerImpl
	implements BehaviorPerformer
{
	@Inject
	private BehaviorResolver resolver;

	@Override
	public Signal performBehavior(Signal signal)
		throws Exception
	{
		SignalBehavior<Signal, Signal> behavior = resolver.behaviorFor(signal);
		Signal reply = behavior.perform(signal);
		return reply;
	}
}