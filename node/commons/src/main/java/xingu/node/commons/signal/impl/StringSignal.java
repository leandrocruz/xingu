package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;


public class StringSignal
	extends SignalSupport
	implements Signal
{
	private String s;

	public StringSignal(String s)
	{
		this.s = s;
	}

	@Override
	public String toString()
	{
		return s;
	}
}