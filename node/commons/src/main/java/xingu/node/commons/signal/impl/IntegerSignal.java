package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;


public class IntegerSignal
	extends SignalSupport
	implements Signal
{
	private int value;

	public IntegerSignal(int i)
	{
		this.value = i;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
}