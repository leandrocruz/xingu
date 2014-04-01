package br.com.ibnetwork.xingu.utils.classloader;

public class ClassLoaderUtils
{
	public static final String nameFor(Class<?> clazz)
	{
		ClassLoader cl = clazz.getClassLoader();
		if(cl instanceof NamedClassLoader)
		{
			return ((NamedClassLoader) cl).getName();
		}
		return null; 
	}
}
