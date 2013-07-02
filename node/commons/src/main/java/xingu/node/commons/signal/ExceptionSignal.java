package xingu.node.commons.signal;

import xingu.node.commons.signal.behavior.SignalBehavior;

public class ExceptionSignal
	extends SignalSupport
	implements SignalBehavior<ExceptionSignal, BooleanSignal>
{
	private Signal		signal;

	private String		trace;

	public ExceptionSignal()
	{}

	public ExceptionSignal(Signal signal, String trace)
	{
		this.signal = signal;
		this.trace  = trace;
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

}