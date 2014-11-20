package xingu.cloud.vm;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import xingu.cloud.vm.impl.google.GCloudSpawner;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class SpawnerTest
	extends XinguTestCase
{
	@Inject
	private Spawner spawner;

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("gcloud-8439692273676268084.txt");
		String output = IOUtils.toString(is);
		binder.bind(Spawner.class).to(GCloudSpawner.class);
		binder.bind(ProcessManager.class).to(new FakeProcessManager(0, output, null));
	}

	@Test
	public void testSpawn()
		throws Exception
	{
		SpawnRequest req = SpawnRequestFactory
				.builder()
				.withZone("us-central1-a")
				.withProject("oystrbots-test")
				.withGroup("sample")
				.withName("oystr-sample")
				.withMachineType("f1-micro")
				.withImage("debian-7")
				.get();

		List<VirtualMachine> machines = spawner.spawn(req);
	}
}
