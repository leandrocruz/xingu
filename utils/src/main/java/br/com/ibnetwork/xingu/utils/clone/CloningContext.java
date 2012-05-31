package br.com.ibnetwork.xingu.utils.clone;

import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CloningContext
{
	private int level = 0;
	
	private Map<Object, Object> cache = new IdentityHashMap<Object, Object>();
	
	public <T> void addReference(T original, T clone)
	{
		Object cached = cache.get(original);
		if(cached != null)
		{
			throw new IllegalArgumentException("Can't add the another clone for " + original);
		}
		cache.put(original, clone);
	}

	@SuppressWarnings("unchecked")
	public <T> T getReference(T original)
	{
		return (T) cache.get(original);
	}

	public void increment()
	{
		level += 1;
	}

	public void decrement()
	{
		level -= 1;
	}

	public String level()
	{
		return StringUtils.leftPad(String.valueOf(level + 1), level * 4, "") + ": ";
	}

}
