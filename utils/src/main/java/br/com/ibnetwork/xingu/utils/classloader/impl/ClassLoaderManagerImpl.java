package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public class ClassLoaderManagerImpl
	implements ClassLoaderManager
{
	private Map<String, SimpleClassLoader> byId = new HashMap<String, SimpleClassLoader>();
	
	@Override
	public SimpleClassLoader byId(String id)
	{
		if(id == null)
		{
			return ContextClassLoaderManager.getClassLoaderManager().byId(id);
		}
		return byId.get(id);
	}

	@Override
	public void register(SimpleClassLoader cl)
	{
		byId.put(cl.id(), cl);
	}
}
