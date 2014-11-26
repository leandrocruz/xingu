package xingu.cloud.spawner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;

public abstract class SpawnerSupport
	implements Spawner
{
	protected Map<String, Surrogate>	surrogateById	= new HashMap<String, Surrogate>();

	protected Logger				logger			= LoggerFactory.getLogger(getClass());

	@Override
	public List<Surrogate> list()
	{
		Set<String>     keys   = surrogateById.keySet();
		List<Surrogate> result = new ArrayList<Surrogate>(keys.size());
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
	public List<Surrogate> spawn(SpawnRequest req)
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

		logger.info("Spawning {} surrogates", count);
		List<Surrogate> surrogates = spawn(req, ids);
		for(Surrogate surrogate : surrogates)
		{
			String id = surrogate.getId();
			logger.info("Surrogate s#{} spawned", id);
			surrogateById.put(id, surrogate);
		}
		return surrogates;
	}

	protected abstract List<Surrogate> spawn(SpawnRequest req, String... ids)
		throws Exception;

	@Override
	public Surrogate attach(String id, Channel channel)
	{
		Surrogate surrogate = surrogateById.get(id);
		logger.info("Attaching surrogate s#{} to addr#{}", id, channel.getRemoteAddress());
		surrogate.setChannel(channel);
		return surrogate;
	}
	
	@Override
	public void dettach(Surrogate surrogate)
	{
		String id = surrogate.getId();
		surrogateById.remove(id);
	}
}