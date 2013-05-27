package xingu.node.commons.signal;

public interface SignalBehavior<T extends Signal, S extends Signal>
{
	S perform(T signal)
		throws Exception;
}
