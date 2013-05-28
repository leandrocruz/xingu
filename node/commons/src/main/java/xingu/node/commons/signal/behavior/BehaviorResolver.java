package xingu.node.commons.signal.behavior;

import xingu.node.commons.signal.Signal;

public interface BehaviorResolver
{
	<T extends Signal, S extends Signal> SignalBehavior<T, S> behaviorFor(Signal signal);
}
