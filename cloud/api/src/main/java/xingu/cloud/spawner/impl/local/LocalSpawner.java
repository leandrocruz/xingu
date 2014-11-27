package xingu.cloud.spawner.impl.local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.SpawnerSupport;

public class LocalSpawner
	extends SpawnerSupport
{
	private File root = new File("/tmp/oystr/meta");

	@Override
	protected List<Surrogate> spawn(SpawnRequest req, String... ids)
		throws Exception
	{
		FileUtils.writeStringToFile(new File(root, "host"), "127.0.0.1");
		FileUtils.writeStringToFile(new File(root, "port"), "8899");
		
		String all  = StringUtils.join(ids, ",");
		FileUtils.writeStringToFile(new File(root, "instance-ids"), all);
		List<Surrogate> result = new ArrayList<>(ids.length);
		for(String id : ids)
		{
			result.add(new LocalSurrogate(id));
		}
		return result;
	}

	@Override
	public synchronized void release(Surrogate surrogate)
	{
		String id = surrogate.getId();
		logger.info("Releasing Surrogate s#{}", id);
	}

}