package br.com.ibnetwork.xingu.utils.io.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ZipUtils
{
	public static void explode(File file, File to)
		throws Exception
	{
		ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if(entry.isDirectory())
			{
				continue;
			}
			//System.out.println("Extracting: " + entry);
			File target = new File(to, entry.getName());
			File parent = target.getParentFile();
			if(!parent.exists())
			{
				parent.mkdirs();
			}
			BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(target));
			IOUtils.copy(is, os);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		zipFile.close();
	}

	public static void zip(File fileOrDirectory, boolean includeRoot, File to)
		throws Exception
	{
		new ZipBuilder(fileOrDirectory, includeRoot).zipTo(to);
	}
}
