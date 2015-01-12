package xingu.utils.classloader.impl;

import xingu.lang.NotImplementedYet;
import xingu.utils.classloader.ClassLoaderManager;
import xingu.utils.classloader.NamedClassLoader;

public class ContextClassLoaderManager
{
	private static ClassLoaderManager INSTANCE;

	static {
		
		final NamedClassLoader wrapper = 
				new ClassLoaderAdapter("context-classloader", Thread.currentThread().getContextClassLoader());
		
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
