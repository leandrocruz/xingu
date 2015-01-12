package xingu.settings.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import xingu.codec.Codec;
import xingu.container.Inject;
import xingu.settings.Settings;
import xingu.settings.SettingsManager;
import xingu.time.Time;
import xingu.utils.DateFormats;

public class SettingsManagerImpl
	implements SettingsManager, Configurable
{
	@Inject("settings")
	private Codec	codec;

	@Inject
	private Time	time;

	private File	root;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		String dir = conf.getChild("repo").getAttribute("root");
		root = new File(dir);
	}

	@Override
	public Settings settingsFor(long owner)
		throws Exception
	{
		File dir    = baseDirectoryFor(owner);
		Date latest = getLatest(dir);
		if(latest == null)
		{
			return null;
		}
		
		String name    = DateFormats.yyyyMMdd_HHmmss.format(latest);
		File   file    = new File(dir, name);
		String encoded = FileUtils.readFileToString(file);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) codec.decode(encoded);		
		return new SettingsImpl(map);
	}

	private File baseDirectoryFor(long owner)
	{
		File dir = new File(root, String.valueOf(owner));
		return dir;
	}

	private Date getLatest(File dir)
		throws ParseException
	{
		if(!dir.exists())
		{
			return null;
		}
		String[] names = dir.list();
		if(names == null || names.length == 0)
		{
			return null;
		}
		
		Date result = null;
		for(String name : names)
		{
			Date date = DateFormats.yyyyMMdd_HHmmss.parse(name);
			if(result == null || date.after(result) || date.equals(result))
			{
				result = date;
			}
		}
		return result;
	}

	@Override
	public void store(long owner, Settings conf)
		throws Exception
	{
		File dir = baseDirectoryFor(owner);
		if(!dir.exists())
		{
			dir.mkdirs();
		}

		Date        now         = time.now().asDate();
		String      name        = DateFormats.yyyyMMdd_HHmmss.format(now);
		File        target      = new File(dir, name);
		Map<String, Object> map = conf.toMap();
		String      encoded     = codec.encode(map);
		FileUtils.write(target, encoded);
	}
}