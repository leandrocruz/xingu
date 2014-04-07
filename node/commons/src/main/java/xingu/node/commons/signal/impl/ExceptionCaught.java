package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.SignalBehavior;

public class ExceptionCaught
	extends SignalSupport
	implements SignalBehavior<ExceptionCaught, Signal>
{
	private Channel		channel;

	private Throwable	cause;

	public ExceptionCaught(Channel channel, Throwable cause)
	{
		this.channel = channel;
		this.cause   = cause;
	}

	public Channel getChannel(){return channel;}
	public void setChannel(Channel channel){this.channel = channel;}
	public Throwable getCause(){return cause;}
	public void setCause(Throwable cause){this.cause = cause;}

	@Override
	public Signal perform(ExceptionCaught signal, Channel channel)
		throws Exception
	{
		cause.printStackTrace();
		return null;
	}
}
