package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;

public class ExceptionCaught
	extends SignalSupport
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
}
