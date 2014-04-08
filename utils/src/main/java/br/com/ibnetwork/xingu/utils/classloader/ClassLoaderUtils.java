package br.com.ibnetwork.xingu.utils.classloader;

import br.com.ibnetwork.xingu.utils.classloader.impl.NamedClassLoader;

public class ClassLoaderUtils
{
	public static final String nameFor(Class<?> clazz)
	{
		ClassLoader cl = clazz.getClassLoader();
		if(cl instanceof NamedClassLoader)
		{
			return ((SimpleClassLoader) cl).id();
		}
		return null; 
	}
}
