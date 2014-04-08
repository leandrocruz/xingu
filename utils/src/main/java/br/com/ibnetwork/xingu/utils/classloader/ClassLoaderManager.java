package br.com.ibnetwork.xingu.utils.classloader;

public interface ClassLoaderManager
{
	SimpleClassLoader byId(String id);

	void register(SimpleClassLoader cl);
}
