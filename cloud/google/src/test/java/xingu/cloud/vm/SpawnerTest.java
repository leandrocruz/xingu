package xingu.cloud.vm;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.SpawnRequestFactory;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.google.GCloudSpawner;
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
	public void testPattern()
	{
		Pattern p = Pattern.compile("(?:[a-z](?:[-a-z0-9]{0,61}[a-z0-9])?)");
		Matcher matcher = p.matcher("gcloud-20141125-114821-abcdef-0");
		assertEquals(true, matcher.find());
		assertEquals(true, matcher.matches());
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
				.withNamePattern("oystr-sample-%d")
				.withIdPattern("id-%d")
				.withMachineType("f1-micro")
				.withImage("debian-7")
				.get(1);

		List<Surrogate> machines = spawner.spawn(req);
		assertEquals("146.148.64.154", machines.get(0).getIp().getAddress());
	}
}