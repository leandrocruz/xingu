package xingu.cloud.spawner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.activity.Startable;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import br.com.ibnetwork.xingu.lang.thread.DaemonThreadFactory;
import br.com.ibnetwork.xingu.lang.thread.ThreadNamer;

public abstract class SpawnerSupport
	implements Spawner, Startable
{
	protected Map<String, Surrogate>	surrogateById	= new HashMap<>();

	protected Logger					logger			= LoggerFactory.getLogger(getClass());

	protected DaemonThreadFactory		tf;
	
	@Override
	public void start()
		throws Exception
	{
		tf = new DaemonThreadFactory(new ThreadNamer(){
			@Override
			public String nameFor(int threadNumber)
			{
				return "Spawner #"+threadNumber;
			}
		});
	}

	@Override
	public void stop()
		throws Exception
	{
		tf.interruptAllThreads();
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

	@Override
	public List<Surrogate> spawn(final SpawnRequest req)
		throws Exception
	{
		int count = req.getCount();
		String[] ids = new String[count];
		for(int i = 0; i < count; i++)
		{
			String pattern = req.getIdPattern();
			String id      = String.format(pattern, i);
			ids[i] = id;
		}

		final List<Surrogate> surrogates = create(ids);

		tf.newThread(new Runnable(){
			@Override
			public void run()
			{
				try
				{
					spawn(req, surrogates);
				}
				catch(Exception e)
				{
					logger.error("Error spawning", e);
				}
			}
		}).start();

		return surrogates;
	}

	private List<Surrogate> create(String[] ids)
	{
		List<Surrogate> result = new ArrayList<>(ids.length);
		for(String id : ids)
		{
			logger.info("Surrogate s#{} created", id);
			Surrogate surrogate = new SurrogateSupport(id);
			surrogateById.put(id, surrogate);
			result.add(surrogate);
		}
		return result;
	}

	protected abstract void spawn(SpawnRequest req, List<Surrogate> surrogates)
		throws Exception;

	@Override
	public Surrogate attach(String id, Channel channel)
	{
		Surrogate surrogate = surrogateById.get(id);
		logger.info("Attaching surrogate s#{} to addr#{}", id, channel.getRemoteAddress());
		if(surrogate == null)
		{
			release(channel);
			return null;
		}
		surrogate.setChannel(channel);
		return surrogate;
	}
	
	protected void release(Channel channel)
	{
		channel.write("close").addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void dettach(Surrogate surrogate)
	{
		String id = surrogate.getId();
		surrogateById.remove(id);
	}
}