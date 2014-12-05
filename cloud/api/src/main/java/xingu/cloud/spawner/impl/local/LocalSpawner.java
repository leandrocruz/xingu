package xingu.cloud.spawner.impl.local;

import java.io.File;
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
	protected void startSurrogate(SpawnRequest req, List<Surrogate> surrogates)
		throws Exception
	{
		FileUtils.writeStringToFile(new File(root, "host"), "127.0.0.1");
		FileUtils.writeStringToFile(new File(root, "port"), "8899");
		
		String[] ids = new String[surrogates.size()];
		int i = 0;
		for(Surrogate surrogate : surrogates)
		{
			ids[i++] = surrogate.getId();
		}
		String all  = StringUtils.join(ids, ",");
		FileUtils.writeStringToFile(new File(root, "instance-ids"), all);
	}

	@Override
	protected void stopSurrogate(Surrogate surrogate)
		throws Exception
	{}
}