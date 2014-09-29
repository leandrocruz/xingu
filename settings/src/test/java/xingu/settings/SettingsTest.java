package xingu.settings;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Test;

import xingu.codec.Codec;
import xingu.codec.impl.JacksonCodec;
import xingu.settings.impl.SettingsManagerImpl;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.utils.io.FileUtils;

public class SettingsTest
	extends XinguTestCase
{
	@Inject
	private SettingsManager settings;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		File src = getFile("settings");
		System.err.println(src);
		File temp = FileUtils.createTempDir("oystr-settings-");
		org.apache.commons.io.FileUtils.copyDirectory(src, temp);
		String path = temp.getAbsolutePath();
		String xml = "<x><repo root=\"" + path + "\" /></x>";
		Configuration conf = buildFrom(xml);

		binder.bind(SettingsManager.class).to(SettingsManagerImpl.class).with(conf);
		binder.bind(Codec.class, "settings").to(JacksonCodec.class);

		System.err.println("Clone directory: " + path);
	}


	@Test
	public void testString()
		throws Exception
	{
		Settings conf = settings.settingsFor(1);
		String   s   = conf.getString("a/s");
		assertEquals("Olá", s);
	}

	@Test
	public void testInteger()
		throws Exception
	{
		Settings conf = settings.settingsFor(1);
		int i   = conf.getInt("a/i");
		assertEquals(1, i);
	}

	@Test
	public void testChangeValue()
		throws Exception
	{
		Settings conf = settings.settingsFor(1);
		String   s   = conf.getString("a/s");
		assertEquals("Olá", s);

		conf.set("a/s", "Replacement");
		s = conf.getString("a/s");
		assertEquals("Replacement", s);
		
		settings.store(1, conf);
	}
}
