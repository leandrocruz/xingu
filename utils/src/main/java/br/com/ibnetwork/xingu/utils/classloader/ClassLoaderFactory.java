package br.com.ibnetwork.xingu.utils.classloader;


public interface ClassLoaderFactory
{
	ClassLoader buildClassLoader(ClassLoader parent)
		throws Exception;
}
