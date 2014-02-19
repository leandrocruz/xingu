package xingu.node.commons.signal;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.behavior.SignalBehavior;
import xingu.node.commons.signal.impl.SignalSupport;

public class SignalWithBehavior
	extends SignalSupport
	implements SignalBehavior<SignalWithBehavior, SignalWithBehavior>
{
	private int value;
	
	public SignalWithBehavior()
	{}

	public SignalWithBehavior(int value)
	{
		this.value = value;
	}
	
	@Override
	public SignalWithBehavior perform(SignalWithBehavior signal, Channel channel)
		throws Exception
	{
		value++;
		return this;
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
