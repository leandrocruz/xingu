package xingu.node.commons.signal.processor.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.jboss.netty.channel.Channel;

import xingu.node.commons.signal.Signal;
import xingu.node.commons.signal.impl.SignalTaskImpl;
import xingu.node.commons.signal.processor.SignalProcessor;
import xingu.node.commons.signal.processor.SignalTask;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public abstract class SignalProcessorSupport
	implements SignalProcessor, Configurable, Startable
{
	@Inject
	protected Factory						factory;

	protected ExecutorService				executor;

	protected Class<? extends SignalTask>	taskClass;

	protected boolean						isRunning	= true;

	@SuppressWarnings("unchecked")
	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String taskClass = conf.getChild("task").getAttribute("class", null);
		if(taskClass != null)
		{
			this.taskClass = (Class<? extends SignalTask>) ObjectUtils.loadClass(taskClass);
		}
		else
		{
			this.taskClass = SignalTaskImpl.class;
		}
	}

	@Override
	public void start()
		throws Exception
	{
		executor = getExecutor();
	}

	@Override
	public void stop()
		throws Exception
	{
		isRunning = false;
		executor.shutdown();
	}

	protected abstract ExecutorService getExecutor();

	@Override
	public Future<Signal> process(Signal signal, Channel channel)
		throws Exception
	{
		if(isRunning)
		{
			Callable<Signal> task = factory.create(taskClass, signal, channel);
			return execute(task);
		}
		else
		{
			return null;
		}
	}

	protected Future<Signal> execute(Callable<Signal> task)
	{
		return executor.submit(task);
	}
}