package br.com.ibnetwork.xingu.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class CopyDirectory
{
	private boolean debug = false;
	
	private FileFilter filter;

	public CopyDirectory()
	{}

	public CopyDirectory(FileFilter filter)
	{
		this.filter = filter;
	}

	public void copyTree(File from, File to)
		throws IOException
	{
		File[] files = from.listFiles(filter);
		for (File file : files)
		{
			File sibling = new File(to, file.getName());
			if(file.isDirectory())
			{
				//create sibling directory
				debug("\n[D] from: " + file + "\n[D] to: " + sibling);
				if(!sibling.exists())
				{
					sibling.mkdirs();
				}
				copyTree(file, sibling);
			}
			else
			{
				//copy file
				debug("\n[F] from: " + file + "\n[F] to: " + sibling);
				int result = copyFile(file, sibling);
				String msg = result > 0 ? FileUtils.byteCountToDisplaySize(result) + " copied" : "error"; 
				debug("[F] " + msg);
			}
		}
	}

	private void debug(String msg)
	{
		if(debug)
		{
			System.out.println(msg);
		}
	}

	private int copyFile(File from, File to)
	{
		InputStream is = null;
		OutputStream os = null;
		try
		{
			is = new FileInputStream(from);
			os = new FileOutputStream(to);
			return IOUtils.copy(is, os);
		}
		catch(Throwable t)
		{
			return -1;
		}
		finally
		{
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
}
