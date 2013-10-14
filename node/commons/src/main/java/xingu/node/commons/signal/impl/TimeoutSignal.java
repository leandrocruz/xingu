package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;


public class TimeoutSignal
	extends SignalSupport
{
	private Signal	signal;

	private long	timeout;

	public TimeoutSignal(Signal signal, long timeout)
	{
		this.signal  = signal;
		this.timeout = timeout;
	}
	
	public Signal getSignal()
	{
		return signal;
	}

	public long getTimeout()
	{
		return timeout;
	}

	@Override
	public String toString()
	{
		return "Timeout for " + signal;
	}
}