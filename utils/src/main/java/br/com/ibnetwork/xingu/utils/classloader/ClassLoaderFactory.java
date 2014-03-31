package br.com.ibnetwork.xingu.utils.classloader;


public interface ClassLoaderFactory
{
	NamedClassLoader buildClassLoader(String name, ClassLoader parent)
		throws Exception;
}
