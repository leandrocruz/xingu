package xingu.node.commons.signal.impl;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.SignalBehavior;

public class ExceptionSignal
	extends SignalSupport
	implements SignalBehavior<ExceptionSignal, BooleanSignal>
{
	private Signal		signal;

	private String		message;

	private String		trace;

	private Throwable	cause;

	public ExceptionSignal()
	{}

	public ExceptionSignal(Signal signal, String message, String trace)
	{
		this.signal  = signal;
		this.message = message;
		this.trace   = trace;
	}

	/*
	 * Don't use this over the wire
	 */
	public ExceptionSignal(Signal signal, Throwable cause)
	{
		this.signal = signal;
		this.message = cause.getMessage();
		this.cause  = cause;
	}

	@Override
	public BooleanSignal perform(ExceptionSignal signal, Channel channel)
		throws Exception
	{
		System.err.println("Error Executing Signal " + this.signal);
		System.err.println(trace);
		return null;
	}

	/* @formatter:off */
	public Signal getSignal(){return signal;}
	public void setSignal(Signal signal){this.signal = signal;}
	public String getTrace(){return trace;}
	public void setTrace(String trace){this.trace = trace;}
	public Throwable getCause(){return cause;}
	public void setCause(Throwable cause){this.cause = cause;}
	public String getMessage(){return message;}
	public void setMessage(String message){this.message = message;}
	/* @formatter:on */
}