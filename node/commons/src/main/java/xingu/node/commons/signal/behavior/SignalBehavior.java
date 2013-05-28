package xingu.node.commons.signal.behavior;

import xingu.node.commons.signal.Signal;

public interface SignalBehavior<T extends Signal, S extends Signal>
{
	S perform(T signal)
		throws Exception;
}
