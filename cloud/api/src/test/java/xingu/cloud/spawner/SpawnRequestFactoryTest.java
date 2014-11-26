package xingu.cloud.spawner;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.NameValue;

public class SpawnRequestFactoryTest
{
	@Test
	public void testCopy()
		throws Exception
	{
		SpawnRequestFactory original = SpawnRequestFactory
			.builder()
			.withRegion("region")
			.withNamespace("namespace")
			.withMachineType("machine")
			.withImage("image")
			.withMeta("x", "y");
	
		SpawnRequestFactory clone = original.copy().withRegion("region2");
		
		SpawnRequest req = original.get(1);
		List<NameValue<String>> meta = req.getMeta();
		assertEquals("region", req.getRegion());
		assertEquals("namespace", req.getNamespace());
		assertEquals("machine", req.getMachineType());
		assertEquals("image", req.getImage());
		assertEquals("x", meta.get(0).name);
		assertEquals("y", meta.get(0).value);
		
		SpawnRequest req2 = clone.get(1);
		List<NameValue<String>> meta2 = req2.getMeta();
		assertEquals("region2", req2.getRegion());
		assertEquals("namespace", req2.getNamespace());
		assertEquals("machine", req2.getMachineType());
		assertEquals("image", req2.getImage());
		assertEquals("x", meta2.get(0).name);
		assertEquals("y", meta2.get(0).value);
		
		assertNotSame(req, req2);
		assertNotSame(meta, meta2);
		
	}
}
