package br.com.ibnetwork.xingu.utils.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class NamedClassLoader
	extends URLClassLoader
{
	private final String name;

	public NamedClassLoader(String name, URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "'"+name+"' ClassLoader @ " + System.identityHashCode(this);
	}
}