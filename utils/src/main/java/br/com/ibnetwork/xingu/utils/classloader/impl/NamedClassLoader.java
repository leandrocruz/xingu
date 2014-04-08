package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.net.URL;
import java.net.URLClassLoader;

import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public class NamedClassLoader
	extends URLClassLoader
	implements SimpleClassLoader
{
	private final String id;

	public NamedClassLoader(String name, URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
		this.id = name;
	}

	@Override
	public String toString()
	{
		return "'"+id+"' ClassLoader @ " + System.identityHashCode(this);
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public ClassLoader getClassLoader()
	{
		return this;
	}
}