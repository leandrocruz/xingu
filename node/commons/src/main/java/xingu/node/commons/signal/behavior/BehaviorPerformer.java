package xingu.node.commons.signal.behavior;

import xingu.node.commons.signal.Signal;

public interface BehaviorPerformer
{
	Signal performBehavior(Signal signal)
		throws Exception;
}
