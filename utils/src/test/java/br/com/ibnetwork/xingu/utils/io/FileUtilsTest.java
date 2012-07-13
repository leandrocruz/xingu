package br.com.ibnetwork.xingu.utils.io;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import junit.framework.TestCase;

public class FileUtilsTest
	extends TestCase
{
	public void testToMap()
		throws Exception
	{
		InputStream is = IOUtils.toInputStream("");
		Map<String, String> map = FileUtils.toMap(is);
		assertEquals(0, map.size());

		is = IOUtils.toInputStream("key = val");
		map = FileUtils.toMap(is);
		assertEquals(1, map.size());
		assertEquals("val", map.get("key"));

		is = IOUtils.toInputStream("key =");
		map = FileUtils.toMap(is);
		assertEquals(1, map.size());
		assertEquals(null, map.get("key"));

		is = IOUtils.toInputStream("k1 = v1\n#k2=v2\nk3 = v3");
		map = FileUtils.toMap(is);
		assertEquals(2, map.size());
		assertEquals("v1", map.get("k1"));
		assertEquals(null, map.get("k2"));
		assertEquals("v3", map.get("k3"));

		is = IOUtils.toInputStream("key = val");
		map = FileUtils.toMap(is, ":");
		assertEquals(0, map.size());
		assertEquals(null, map.get("key"));

		is = IOUtils.toInputStream("key: val");
		map = FileUtils.toMap(is, ":");
		assertEquals(1, map.size());
		assertEquals("val", map.get("key"));

	}
}
