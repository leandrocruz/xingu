package xingu.node.commons.signal.processor.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import xingu.lang.NotImplementedYet;
import xingu.lang.thread.InstantFuture;
import xingu.node.commons.signal.Signal;

public class SerialSignalProcessor
    extends SignalProcessorSupport
{

    @Override
    protected ExecutorService getExecutor()
    {
        return null;
    }

	@Override
	protected Future<Signal> execute(Callable<Signal> task)
	{
        try
        {
            Signal s = task.call();
            return new InstantFuture<Signal>(s); 
        }
        catch (Exception e)
        {
            throw new NotImplementedYet();
        }
	}
}