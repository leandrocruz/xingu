package xingu.cloud.spawner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;

public abstract class SpawnerSupport
	implements Spawner
{
	protected Map<String, Entry>	surrogateById	= new HashMap<String, Entry>();

	protected Logger				logger			= LoggerFactory.getLogger(getClass());

	protected Entry entryFor(Surrogate surrogate)
	{
		String id    = surrogate.getId();
		Entry  entry = surrogateById.get(id);
		return entry;
	}

	@Override
	public List<Surrogate> list()
	{
		Set<String>     keys   = surrogateById.keySet();
		List<Surrogate> result = new ArrayList<Surrogate>(keys.size());
		for(String key : keys)
		{
			Entry   entry = surrogateById.get(key);
			result.add(entry.surrogate);
		}
		return result;
	}

	@Override
	public Surrogate byChannel(Channel channel)
	{
		Set<String> keys = surrogateById.keySet();
		for(String key : keys)
		{
			Entry   entry = surrogateById.get(key);
			Channel c     = entry.surrogate.getChannel();
			if(channel.equals(c))
			{
				return entry.surrogate;
			}
		}
		return null;
	}

	@Override
	public int getSurrogateCount()
	{
		return surrogateById.size();
	}

	@Override
	public int getLockCount(String bundle)
	{
		int count = 0;
		Set<String> keys = surrogateById.keySet();
		for(String key : keys)
		{
			Entry   entry  = surrogateById.get(key);
			boolean locked = entry.isLockedFor(bundle);
			if(locked)
			{
				count++;
			}
		}
		return count;
	}

	@Override
	public List<Surrogate> get(int count, String bundle)
	{
		List<Surrogate> result = new ArrayList<Surrogate>(count);
		int i = 0;
		while(i < count)
		{
			i++;
			Surrogate surrogate = nextSurrogate(bundle);
			if(surrogate != null)
			{
				result.add(surrogate);
			}
		}
		return result;
	}

	protected synchronized Surrogate nextSurrogate(String bundle)
	{
		Set<String> keys = surrogateById.keySet();
		for(String key : keys)
		{
			Entry   entry    = surrogateById.get(key);
			boolean attached = entry.surrogate.isAttached();
			if(attached)
			{
				boolean lockGranted = entry.tryLockFor(bundle);
				if(lockGranted)
				{
					logger.info("Allocating Surrogate s#{} to b#{}", key, bundle);
					return entry.surrogate;
				}
			}
		}
		return null;
	}

	@Override
	public synchronized void release(Surrogate surrogate, String bundle)
	{
		Entry  entry = entryFor(surrogate);
		String id    = surrogate.getId();
		logger.info("Releasing Surrogate s#{} from b#{}", id, bundle);
		if(entry != null)
		{
			entry.unlock(bundle);
		}
	}

	@Override
	public List<Surrogate> spawn(SpawnRequest req)
		throws Exception
	{
		int count = req.getCount();
		List<Surrogate> result = new ArrayList<>(count);
		for(int i = 0; i < count; i++)
		{
			String namePattern = req.getNamePattern();
			String name        = String.format(namePattern, i);
			String idPattern   = req.getIdPattern();
			String id          = String.format(idPattern, i);
			
			logger.info("Spawning surrogate s#{}", id);
			Surrogate surrogate = spawn(id, name, req);
			logger.info("Surrogate s#{} spawned", id);
			
			Entry entry = new Entry(surrogate);
			surrogateById.put(id, entry);

			result.add(surrogate);
		}
		return result;
	}

	protected Surrogate spawn(String id, String name, SpawnRequest req)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public synchronized Surrogate attach(String id, Channel channel)
	{
		Entry     entry     = surrogateById.get(id);
		Surrogate surrogate = entry.surrogate;
		logger.info("Attaching surrogate s#{} to addr#{}", id, channel.getRemoteAddress());
		surrogate.setChannel(channel);
		return surrogate;
	}
	
	@Override
	public synchronized void dettach(Surrogate surrogate)
	{
		Entry  entry = entryFor(surrogate);
		String id    = surrogate.getId();
		if(!entry.isLocked())
		{
			logger.warn("Consumer for surrogate s#{} still active", id);
		}
		surrogateById.remove(id);
	}
}