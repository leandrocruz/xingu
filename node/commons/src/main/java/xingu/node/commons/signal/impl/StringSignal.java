package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;


public class StringSignal
	extends SignalSupport
	implements Signal
{
	private String value;

	public StringSignal(String s)
	{
		this.value = s;
	}

	@Override
	public String toString()
	{
		return value;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}