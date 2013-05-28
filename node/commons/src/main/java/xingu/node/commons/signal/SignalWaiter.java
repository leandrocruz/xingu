package xingu.node.commons.signal;

public class SignalWaiter
	extends Waiter<Signal>
{
	private final long id;
	
	public SignalWaiter(Signal signal)
	{
		super();
		id = signal.getSignalId();
	}

	@Override
	public boolean waitingOn(Signal reply)
	{
		return id == reply.getSignalId();
	}
}