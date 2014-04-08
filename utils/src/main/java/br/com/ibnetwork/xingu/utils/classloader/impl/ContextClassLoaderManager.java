package br.com.ibnetwork.xingu.utils.classloader.impl;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;

public class ContextClassLoaderManager
{
	private static ClassLoaderManager INSTANCE;

	static {
		
		final NamedClassLoader wrapper = 
				new ClassLoaderWrapper("context-classloader", Thread.currentThread().getContextClassLoader());
		
		INSTANCE = new ClassLoaderManager()
		{
			@Override
			public NamedClassLoader byId(String classLoaderName)
			{
				return wrapper;
			}

			@Override
			public void register(NamedClassLoader cl)
			{
				throw new NotImplementedYet();
			}
		};
	}

	public static ClassLoaderManager getClassLoaderManager()
	{
		return INSTANCE;
	}
	
}
