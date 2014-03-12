package br.com.ibnetwork.xingu.utils.classloader;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.filefilter.SuffixFileFilter;

public class DirectoryClassLoader
	implements ClassLoaderFactory
{
	private final File	root;

	public DirectoryClassLoader(File root)
	{
		this.root = root;
	}
	
	@Override
	public ClassLoader buildClassLoader(ClassLoader parent)
		throws Exception
	{
		File        libDir = new File(root, "/lib");
		FileFilter  filter = new SuffixFileFilter(".jar");
		File[]      list   = libDir.listFiles(filter);
		URL[]       urls   = new URL[list.length];
		for(int i = 0; i < list.length; i++)
		{
			File file = list[i];
			if(file.isDirectory())
			{
				urls[i] = new URL("file://" + file.getAbsolutePath() + "/");
			}
			else
			{
				urls[i] = new URL("file://" + file.getAbsolutePath());
			}
		}
		return new URLClassLoader(urls, parent);
	}
}
