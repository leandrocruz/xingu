package xingu.cloud.vm;

import java.util.List;

public interface Spawner
{
	List<VirtualMachine> spawn(SpawnRequest req)
		throws Exception;
}
