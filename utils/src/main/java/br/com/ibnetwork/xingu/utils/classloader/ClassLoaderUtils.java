package br.com.ibnetwork.xingu.utils.classloader;

import br.com.ibnetwork.xingu.utils.classloader.impl.NamedClassLoaderImpl;

public class ClassLoaderUtils
{
	public static final String nameFor(Class<?> clazz)
	{
		ClassLoader cl = clazz.getClassLoader();
		if(cl instanceof NamedClassLoaderImpl)
		{
			return ((NamedClassLoader) cl).id();
		}
		return null; 
	}
}
