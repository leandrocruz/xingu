package xingu.utils.classloader.impl;

import java.net.URL;

import xingu.utils.classloader.NamedClassLoader;

public class ClassLoaderAdapter
	implements NamedClassLoader
{
	private String		id;

	private ClassLoader	cl;

	public ClassLoaderAdapter(String id, ClassLoader cl)
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