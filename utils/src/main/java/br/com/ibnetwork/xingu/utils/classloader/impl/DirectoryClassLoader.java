package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;

import org.apache.commons.io.filefilter.SuffixFileFilter;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderFactory;
import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public class DirectoryClassLoader
	implements ClassLoaderFactory
{
	private final File	root;

	public DirectoryClassLoader(File root)
	{
		this.root = root;
	}
	
	@Override
	public SimpleClassLoader buildClassLoader(String name, SimpleClassLoader parent)
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
				urls[i] = new File(file.getParentFile(), file.getName() + "/").toURI().toURL();
			}
			else
			{
				urls[i] = file.toURI().toURL();
			}
		}
		return new NamedClassLoader(name, urls, parent.getClassLoader());
	}
}
