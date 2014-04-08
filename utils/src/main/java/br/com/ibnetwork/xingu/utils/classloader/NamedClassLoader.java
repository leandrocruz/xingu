package br.com.ibnetwork.xingu.utils.classloader;

import java.net.URL;

public interface NamedClassLoader
{
	String id();
	
	Class<?> loadClass(String name)
		throws Exception;

	URL getResource(String string);
	
	ClassLoader getClassLoader();
}
