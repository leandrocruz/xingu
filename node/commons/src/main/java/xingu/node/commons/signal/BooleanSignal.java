package xingu.node.commons.signal;


public class BooleanSignal
	extends SignalSupport
	implements Signal
{
	private Boolean bool;

	public BooleanSignal(Boolean bool)
	{
		this.bool = bool;
	}

	@Override
	public String toString()
	{
		return bool != null ? bool.toString() : "NULL";
	}
}