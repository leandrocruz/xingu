package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.net.URL;
import java.net.URLClassLoader;

import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;

public class NamedClassLoaderImpl
	extends URLClassLoader
	implements NamedClassLoader
{
	private final String id;

	public NamedClassLoaderImpl(String name, URL[] urls, ClassLoader parent)
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