package br.com.ibnetwork.xingu.utils.classloader;

public interface ClassLoaderFactory
{
	SimpleClassLoader buildClassLoader(String name, SimpleClassLoader parent)
		throws Exception;
}
