package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.utils.FSUtils;

public class ConfigurationLoaderTest
{
	@Test
	@Ignore
	public void testMergeConfiguration()
		throws Exception
	{
		File          file = FSUtils.loadAsFile("nebers.xml");
		Configuration conf = ConfigurationLoader.load(file);
		Configuration c0   = conf.getChildren("component")[0];
		assertEquals("A", c0.getAttribute("role"));
		assertEquals(null, c0.getAttribute("key", null));
		assertEquals("a", c0.getAttribute("class"));

		Configuration c1 = conf.getChildren("component")[1];
		assertEquals("X", c1.getAttribute("role"));
		assertEquals(null, c1.getAttribute("key", null));
		assertEquals("OtherX", c1.getAttribute("class"));

	}
}
