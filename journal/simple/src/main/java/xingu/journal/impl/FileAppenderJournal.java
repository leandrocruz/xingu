package xingu.journal.impl;

import java.io.File;
import java.io.FileWriter;

import xingu.journal.Journal;

import org.apache.avalon.framework.activity.Startable;
import org.apache.commons.lang3.SystemUtils;

public class FileAppenderJournal
	extends JournalSupport
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
		File file = File.createTempFile("http-journal-", ".txt", dir);
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