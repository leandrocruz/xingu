package xingu.journal.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import xingu.journal.Journal;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.SystemUtils;

public class FileAppenderJournal
	extends JournalSupport
	implements Journal, Configurable, Startable
{
	private FileWriter	writer;

	private boolean		open		= true;

	private String		output;

	private boolean		truncate	= false;

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		conf = conf.getChild("conf");
		output = conf.getAttribute("output", null);
		truncate = conf.getAttributeAsBoolean("truncate", false);
	}

	@Override
	public void start()
		throws Exception
	{
		File file;
		if(output == null)
		{
			File tmp = SystemUtils.getJavaIoTmpDir();
			File dir = new File(tmp, "journal");
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			file = File.createTempFile("http-journal-", ".txt", dir);
		}
		else
		{
			file = new File(output);
			if(truncate)
			{
				new FileOutputStream(file, false).close();
			}
		}

		System.err.println(file);
		writer = new FileWriter(file, true);
	}
	
	@Override
	public void stop()
		throws Exception
	{
		open = false;
		writer.close();
	}

	@Override
	public synchronized void append(Level level, String string)
		throws Exception
	{
		if(!open)
		{
			return;
		}
		
		writer.append(level + " " + string + "\n");
		writer.flush();
	}
}