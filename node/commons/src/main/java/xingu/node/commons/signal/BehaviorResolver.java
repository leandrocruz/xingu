package xingu.node.commons.signal;

public interface BehaviorResolver
{
	<T extends Signal, S extends Signal> SignalBehavior<T, S> behaviorFor(Signal signal);
}
