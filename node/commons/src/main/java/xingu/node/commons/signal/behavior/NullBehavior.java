package xingu.node.commons.signal.behavior;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;

public class NullBehavior
	implements SignalBehavior<Signal<?>>
{
	private static final SignalBehavior<Signal<?>>	INSTANCE = new NullBehavior();

	private NullBehavior()
	{}

	@Override
	public Signal<?> perform(Signal<?> signal, Channel channel)
	{
		return null;
	}

	public static SignalBehavior<Signal<?>> instance()
	{
		return INSTANCE;
	}
}
