package br.com.ibnetwork.xingu.utils.classloader.impl;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public class ContextClassLoaderManager
{
	private static ClassLoaderManager INSTANCE;

	static {
		
		final SimpleClassLoader wrapper = 
				new ClassLoaderWrapper("context-classloader", Thread.currentThread().getContextClassLoader());
		
		INSTANCE = new ClassLoaderManager()
		{
			@Override
			public SimpleClassLoader byId(String classLoaderName)
			{
				return wrapper;
			}

			@Override
			public void register(SimpleClassLoader cl)
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
