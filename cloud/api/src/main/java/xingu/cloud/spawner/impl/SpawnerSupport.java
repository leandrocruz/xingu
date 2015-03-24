package xingu.cloud.spawner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.avalon.framework.activity.Startable;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.task.SpawnerTask;
import xingu.lang.thread.DaemonThreadFactory;
import xingu.lang.thread.ThreadNamer;

public abstract class SpawnerSupport
	implements Spawner, Startable
{
	protected Map<String, Surrogate>	surrogateById	= new HashMap<>();

	private Queue<SpawnerTask>			queue			= new ConcurrentLinkedQueue<>();

	protected DaemonThreadFactory		tf;

	private boolean						shouldRun		= true;

	protected Logger					logger			= LoggerFactory.getLogger(getClass());
	
	@Override
	public void start()
		throws Exception
	{
		tf = new DaemonThreadFactory(new ThreadNamer(){
			@Override
			public String nameFor(int threadNumber)
			{
				return "Spawner Worker #"+threadNumber;
			}
		});
	
		Thread worker = tf.newThread(new Runnable(){
			@Override
			public void run()
			{
				consumeQueue();
			}
		});
		
		worker.start();
	}

	@Override
	public void stop()
		throws Exception
	{
		tf.interruptAllThreads();
	}

	private void consumeQueue()
	{
		while(shouldRun)
		{
			SpawnerTask task = queue.poll();
			if(task == null)
			{
				try
				{
					synchronized(queue)
					{
						/*
						 * Waiting for 30 secs because there is a chance that we miss the notify() signal
						 */
						queue.wait(30 * 1000);
					}
				}
				catch(InterruptedException e)
				{
					shouldRun = false;
				}
			}
			else
			{
				try
				{
					task.execute();
				}
				catch(Exception e)
				{
					logger.error("Error executing SpawnerTask", e);
				}
			}
		}
	}
	
	@Override
	public List<Surrogate> list()
	{
		Set<String>     keys   = surrogateById.keySet();
		List<Surrogate> result = new ArrayList<>(keys.size());
		for(String key : keys)
		{
			Surrogate surrogate = surrogateById.get(key);
			result.add(surrogate);
		}
		return result;
	}

	@Override
	public Surrogate byChannel(Channel channel)
	{
		Set<String> keys = surrogateById.keySet();
		for(String key : keys)
		{
			Surrogate surrogate = surrogateById.get(key);
			Channel   c         = surrogate.getChannel();
			if(channel.equals(c))
			{
				return surrogate;
			}
		}
		return null;
	}

	private void enqueue(SpawnerTask task)
	{
        synchronized(queue)
        {                           
            queue.add(task);
            queue.notify();
        }
	}

	@Override
	public List<Surrogate> spawn(final SpawnRequest req)
		throws Exception
	{
		String region  = req.getRegion();
		String pattern = req.getIdPattern();
		String tag     = req.getTag();
		int    count   = req.getCount();
		List<Surrogate> surrogates = new ArrayList<>(count);

		for(int i = 0; i < count; i++)
		{
			String    id        = String.format(pattern, tag, i);
			Surrogate surrogate = new SurrogateSupport(id, region);

			logger.info("Surrogate s#{} created", id);
			surrogateById.put(id, surrogate);
			surrogates.add(surrogate);
		}
		
		enqueue(new Start(req, surrogates));
		return surrogates;
	}

	@Override
	public void release(Surrogate surrogate)
		throws Exception
	{
		enqueue(new Stop(surrogate));
	}

	@Override
	public Surrogate attach(String id, Channel channel)
	{
		Surrogate surrogate = surrogateById.get(id);
		if(surrogate == null)
		{
			release(channel);
			return null;
		}
		logger.info("Attaching surrogate s#{} to addr#{}", id, channel.getRemoteAddress());
		surrogate.setChannel(channel);
		return surrogate;
	}
	
	protected void release(Channel channel)
	{
		logger.info("Releasing channel#{}", channel.getRemoteAddress());
		channel.write("close").addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void dettach(Surrogate surrogate)
	{
		String id = surrogate.getId();
		surrogateById.remove(id);
	}

	protected abstract void startSurrogate(SpawnRequest req, List<Surrogate> surrogates)
		throws Exception;

	protected abstract void stopSurrogate(Surrogate surrogate)
		throws Exception;

	class Start
		implements SpawnerTask
	{
		private SpawnRequest	req;

		private List<Surrogate>	surrogates;

		public Start(SpawnRequest req, List<Surrogate> surrogates)
		{
			this.req = req;
			this.surrogates = surrogates;
		}

		@Override
		public void execute()
			throws Exception
		{
			logger.info("Starting {} Surrogates", surrogates.size());
			startSurrogate(req, surrogates);
		}
	}
	
	class Stop
		implements SpawnerTask
	{
		private Surrogate	surrogate;

		public Stop(Surrogate surrogate)
		{
			this.surrogate = surrogate;
		}

		@Override
		public void execute()
			throws Exception
		{
			logger.info("Releasing Surrogate s#{}", surrogate.getId());
			stopSurrogate(surrogate);

			boolean attached = surrogate.isAttached();
			if(attached)
			{
				Channel channel = surrogate.getChannel();
				release(channel);
			}
		}
	}
}