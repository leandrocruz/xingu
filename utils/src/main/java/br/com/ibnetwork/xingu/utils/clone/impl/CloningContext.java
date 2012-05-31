package br.com.ibnetwork.xingu.utils.clone.impl;

import java.util.IdentityHashMap;
import java.util.Map;

class CloningContext
{
	private Map<Object, Object> cache = new IdentityHashMap<Object, Object>();
	
	<T> void add(T original, T clone)
	{
		Object cached = cache.get(original);
		if(cached != null)
		{
			throw new IllegalArgumentException("Can't add the another clone for " + original);
		}
		cache.put(original, clone);
	}

	@SuppressWarnings("unchecked")
	<T> T get(T original)
	{
		return (T) cache.get(original);
	}
}
