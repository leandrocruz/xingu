package br.com.ibnetwork.xingu.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class CopyDirectory
{
	public void copyTree(File from, File to)
		throws IOException
	{
		File[] files = from.listFiles();
		for (File file : files)
		{
			File sibling = new File(to, file.getName());
			if(file.isDirectory())
			{
				//create sibling directory
				System.out.println("\n[D] from: " + file + "\n[D] to: " + sibling);
				if(!sibling.exists())
				{
					sibling.mkdirs();
				}
				copyTree(file, sibling);
			}
			else
			{
				//copy file
				System.out.println("\n[F] from: " + file + "\n[F] to: " + sibling);
				int result = copyFile(file, sibling);
				String msg = result > 0 ? FileUtils.byteCountToDisplaySize(result) + " copied" : "error"; 
				System.out.println("[F] " + msg);
			}
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
