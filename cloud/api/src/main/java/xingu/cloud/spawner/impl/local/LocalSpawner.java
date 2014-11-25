package xingu.cloud.spawner.impl.local;

import java.io.File;

import org.apache.commons.io.FileUtils;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.SpawnerSupport;

public class LocalSpawner
	extends SpawnerSupport
{
	@Override
	protected Surrogate spawn(String id, String name, SpawnRequest req)
		throws Exception
	{
		File file = new File("/tmp/spawn");
		FileUtils.writeStringToFile(file, id);
		return new LocalSurrogate(id);
	}
}