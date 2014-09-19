package xingu.node.commons.signal.behavior;

import xingu.node.commons.signal.Signal;

public interface BehaviorResolver
{
	<T extends Signal<?>> SignalBehavior<T> behaviorFor(Signal<?> signal);
}
