package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;


public class LongSignal
	extends SignalSupport
	implements Signal
{
	private long value;

	public LongSignal(long i)
	{
		this.value = i;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}

	public long getValue()
	{
		return value;
	}

	public void setValue(long value)
	{
		this.value = value;
	}
}