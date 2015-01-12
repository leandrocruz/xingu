package xingu.node.commons.signal.processor.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xingu.lang.thread.DaemonThreadFactory;
import xingu.lang.thread.SimpleThreadNamer;

public class CachedThreadedSignalProcessor
    extends SignalProcessorSupport
{
	private DaemonThreadFactory	tf;

	@Override
	public void start()
		throws Exception
	{
    	tf = new DaemonThreadFactory(new SimpleThreadNamer("Signal Processor Worker"));
    	super.start();
	}

	@Override
	public void stop()
		throws Exception
	{
		tf.interruptAllThreads();
		super.stop();
	}

	@Override
    protected ExecutorService getExecutor()
    {
		return Executors.newFixedThreadPool(5, tf);
    }
}