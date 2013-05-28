package xingu.node.commons.signal.processor.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;
import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;
import br.com.ibnetwork.xingu.lang.thread.SimpleThreadNamer;

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
    
    @Override
    public Future<Signal> process(Signal signal, Channel channel) 
        throws Exception
    {
        Future<Signal> future = super.process(signal, channel);
        if(signal.isSync())
        {
            future.get();
        }
        return future;
    }
}