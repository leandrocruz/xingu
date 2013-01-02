package xingu.journal.impl;

import java.io.File;
import java.io.FileWriter;

import org.apache.avalon.framework.activity.Startable;
import org.apache.commons.lang3.SystemUtils;

import xingu.journal.Journal;

public class FileAppenderJournal
	implements Journal, Startable
{
	private FileWriter writer;
	
	private boolean open = true;
	
	@Override
	public void start()
		throws Exception
	{
		File tmp = SystemUtils.getJavaIoTmpDir();
		File dir = new File(tmp, "journal");
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		File file = File.createTempFile("http-journal_", ".txt", dir);
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
	public synchronized void append(String string)
		throws Exception
	{
		append(string, true);
	}

	@Override
	public synchronized void append(String string, boolean withSeparator)
		throws Exception
	{
		if(!open)
		{
			return;
		}
		
		if(withSeparator)
		{
			writer.append(string);
			writer.append("\n-----------------------------------------------------------------------------\n");
		}
		else
		{
			writer.append(string + "\n");
		}
		writer.flush();
	}
}