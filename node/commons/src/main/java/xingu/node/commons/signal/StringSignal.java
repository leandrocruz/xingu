package xingu.node.commons.signal;

import xingu.node.commons.signal.behavior.SignalBehavior;

public class StringSignal
	extends SignalSupport
	implements Signal, SignalBehavior<StringSignal, StringSignal>
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

	@Override
	public StringSignal perform(StringSignal signal)
		throws Exception
	{
		s = s.toUpperCase();
		System.out.println(s);
		return this;
	}
}