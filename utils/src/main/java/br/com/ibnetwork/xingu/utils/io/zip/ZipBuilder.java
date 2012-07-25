package br.com.ibnetwork.xingu.utils.io.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipBuilder
{
	private final File root;
	
	private int rootPathSize;

	private final boolean includeRoot;
	
	public ZipBuilder(File root, boolean includeRoot)
	{
		this.root = root;
		this.includeRoot = includeRoot;
		if(root.isDirectory())
		{
			if(includeRoot)
			{
				this.rootPathSize = root.getAbsoluteFile().getParent().length() + 1;
			}
			else
			{
				this.rootPathSize = root.getAbsolutePath().length() + 1;
			}
		}
		else
		{
			this.rootPathSize = root.getAbsolutePath().length() + 1;
		}
	}

	public void zipTo(File to)
		throws IOException
	{
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(to));
		process(zos, root);
		IOUtils.closeQuietly(zos);
	}

	private void process(ZipOutputStream zos, File file)
		throws IOException
	{
		if(!file.equals(root) || includeRoot)
		{
			add(zos, file);
		}
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			for (File f : files)
			{
				process(zos, f);
			}
		}
	}
	
	private void add(ZipOutputStream zos, File file)
		throws IOException
	{
		ZipEntry entry = entryFor(file);
		//System.out.println("Adding: " + file.getAbsoluteFile() + " to: " + entry);
		zos.putNextEntry(entry);
		if(file.isFile())
		{
			InputStream is = new FileInputStream(file);
			IOUtils.copy(is, zos);
			IOUtils.closeQuietly(is);
		}
		zos.closeEntry();
	}

	private ZipEntry entryFor(File file)
	{
		String path = file.getAbsolutePath();
		String name = path.substring(rootPathSize);
		if(file.isDirectory())
		{
			name += "/";
		}
		return new ZipEntry(name);
	}
}
