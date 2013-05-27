package xingu.node.commons.signal;

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