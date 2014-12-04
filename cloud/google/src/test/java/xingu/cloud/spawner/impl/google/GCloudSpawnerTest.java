package xingu.cloud.spawner.impl.google;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import xingu.cloud.spawner.SpawnRequest;
import xingu.cloud.spawner.SpawnRequestFactory;
import xingu.cloud.spawner.Spawner;
import xingu.cloud.spawner.Surrogate;
import xingu.cloud.spawner.impl.google.GCloudSpawner;
import xingu.codec.Codec;
import xingu.codec.impl.JacksonCodec;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class GCloudSpawnerTest
	extends XinguTestCase
{
	@Inject
	private Spawner spawner;

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("create.json");
		String output = IOUtils.toString(is);
		binder.bind(Spawner.class).to(GCloudSpawner.class);
		binder.bind(ProcessManager.class).to(new FakeProcessManager(0, output, null));
		binder.bind(Codec.class, "gcloud").to(JacksonCodec.class);
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
	@Ignore
	public void testSpawn()
		throws Exception
	{
		SpawnRequest req = SpawnRequestFactory
				.builder()
				.withIdPattern("xxx")
				.get(1);

		List<Surrogate> surrogates = spawner.spawn(req);
		assertEquals(1, surrogates.size());

		Surrogate surrogate = surrogates.get(0);
		assertEquals("oystr-nebers-2", surrogate.getId());
		assertEquals("130.211.143.135", surrogate.getIp().getAddress());
		assertEquals("us-central1-a", surrogate.getRegion());
	}
}