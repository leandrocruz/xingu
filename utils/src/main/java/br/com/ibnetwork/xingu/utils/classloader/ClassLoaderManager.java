package br.com.ibnetwork.xingu.utils.classloader;

public interface ClassLoaderManager
{
	NamedClassLoader byId(String id);

	void register(NamedClassLoader cl);
}
