package xingu.node.commons.signal;

public class SignalWaiter
	extends Waiter<Signal>
{
	public SignalWaiter(Signal request)
	{
		super(request);
	}

	@Override
	public boolean waitingOn(Signal reply)
	{
		return request.getSignalId() == reply.getSignalId();
	}
}