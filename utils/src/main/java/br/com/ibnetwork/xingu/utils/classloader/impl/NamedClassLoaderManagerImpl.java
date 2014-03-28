package br.com.ibnetwork.xingu.utils.classloader.impl;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoaderManager;

public class NamedClassLoaderManagerImpl
	implements NamedClassLoaderManager
{
	@Override
	public ClassLoader byName(String name)
	{
		if("system".equals(name))
		{
			return Thread.currentThread().getContextClassLoader();
		}
		throw new NotImplementedYet("Can't find NamedClassLoader for " + name);
	}
}
