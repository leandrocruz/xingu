package br.com.ibnetwork.xingu.utils.classloader.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;

public class ClassLoaderManagerImpl
	implements ClassLoaderManager
{
	private Map<String, NamedClassLoader> byId = new HashMap<String, NamedClassLoader>();
	
	@Override
	public NamedClassLoader byId(String id)
	{
		if(id == null)
		{
			return ContextClassLoaderManager.getClassLoaderManager().byId(id);
		}
		return byId.get(id);
	}

	@Override
	public void register(NamedClassLoader cl)
	{
		byId.put(cl.id(), cl);
	}
}
