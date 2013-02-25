package br.com.ibnetwork.xingu.utils.cache.impl;

import java.util.Iterator;

import br.com.ibnetwork.xingu.utils.cache.CacheStatus;
import br.com.ibnetwork.xingu.utils.cache.Recyclable;
import br.com.ibnetwork.xingu.utils.cache.RecyclableCache;

public class NullCache<T extends Recyclable>
	implements RecyclableCache<T>
{
	@Override
	public T next()
	{
		return null;
	}

	@Override
	public void returnItem(T t)
	{}

	@Override
	public void using(T t)
	{}

	@Override
	public void dispose()
	{}

	@Override
	public CacheStatus status()
	{
		return new NullCacheSatus();
	}

	@Override
	public Iterator<T> iterator()
	{
		return null;
	}
}

class NullCacheSatus
	implements CacheStatus
{
	@Override
	public int cached()
	{
		return 0;
	}

	@Override
	public int taken()
	{
		return 0;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public int capacity()
	{
		return 0;
	}
}