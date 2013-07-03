package xingu.node.commons.signal;


public class TimeoutSignal
	extends SignalSupport
{
	private Signal	signal;

	public TimeoutSignal(Signal signal)
	{
		this.signal = signal;
	}

	@Override
	public String toString()
	{
		return "Timeout for " + signal;
	}
}