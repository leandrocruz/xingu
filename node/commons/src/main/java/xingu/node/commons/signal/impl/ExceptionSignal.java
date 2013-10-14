package xingu.node.commons.signal.impl;

import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.behavior.SignalBehavior;

public class ExceptionSignal
	extends SignalSupport
	implements SignalBehavior<ExceptionSignal, BooleanSignal>
{
	private Signal		signal;

	private String		trace;

	private Throwable	cause;

	public ExceptionSignal()
	{}

	public ExceptionSignal(Signal signal, String trace)
	{
		this.signal = signal;
		this.trace  = trace;
	}

	/*
	 * Don't use this over the wire
	 */
	public ExceptionSignal(Signal signal, Throwable cause)
	{
		this.signal = signal;
		this.cause = cause;
	}

	@Override
	public BooleanSignal perform(ExceptionSignal signal)
		throws Exception
	{
		System.err.println("Error Executing Signal " + this.signal);
		System.err.println(trace);
		return null;
	}

	public Signal getSignal(){return signal;}
	public void setSignal(Signal signal){this.signal = signal;}
	public String getTrace(){return trace;}
	public void setTrace(String trace){this.trace = trace;}
	public Throwable getCause(){return cause;}
	public void setCause(Throwable cause){this.cause = cause;}
}