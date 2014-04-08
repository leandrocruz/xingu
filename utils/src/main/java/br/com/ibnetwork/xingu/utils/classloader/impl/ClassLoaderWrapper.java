package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.net.URL;

import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public class ClassLoaderWrapper
	implements SimpleClassLoader
{
	private String		id;

	private ClassLoader	cl;

	public ClassLoaderWrapper(String id, ClassLoader cl)
	{
		this.id = id;
		this.cl = cl;
	}
	
	@Override
	public Class<?> loadClass(String name)
		throws Exception
	{
		return cl.loadClass(name);
	}
	
	@Override
	public String id()
	{
		return id;
	}
	@Override
	public URL getResource(String name)
	{
		return cl.getResource(name);
	}
	
	@Override
	public ClassLoader getClassLoader()
	{
		return cl;
	}
}