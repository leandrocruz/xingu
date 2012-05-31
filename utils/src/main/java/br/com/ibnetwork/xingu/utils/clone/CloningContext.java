package br.com.ibnetwork.xingu.utils.clone;

import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CloningContext
{
	private int level = 0;
	
	private Map<Object, Object> cache = new IdentityHashMap<Object, Object>();

	private String name;

	private final boolean pretty;
	
	public CloningContext(boolean pretty)
	{
		this.pretty = pretty;
	}

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

	public CloningContext setName(String name)
	{
		this.name = name;
		return this;
	}

	public CloningContext clearName()
	{
		this.name = null;
		return this;
	}

	public void print(Object original)
	{
		if(!pretty)
		{
			return;
		}

		String ident = StringUtils.leftPad("", level * 4);
		String className;
		Class<?> clazz = original.getClass();
		if(StringUtils.isNotEmpty(name))
		{
			className = name + ":" + clazz.getSimpleName();
		}
		else
		{
			className = clazz.getSimpleName();
		}
		
		String value = original.toString();
		if(clazz.isArray())
		{
			value = "ARRAY";
		}
		
		System.out.println(ident + className + " = " + value);
	}

	public void start()
	{
		if(!pretty)
		{
			return;
		}
		System.out.println("-");
	}

	public void end()
	{
		if(!pretty)
		{
			return;
		}
		System.out.println(".");

	}
}
