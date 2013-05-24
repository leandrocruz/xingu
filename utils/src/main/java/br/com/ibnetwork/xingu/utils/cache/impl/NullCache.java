package br.com.ibnetwork.xingu.utils.cache.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.ibnetwork.xingu.utils.cache.CacheStatus;
import br.com.ibnetwork.xingu.utils.cache.Recyclable;
import br.com.ibnetwork.xingu.utils.cache.RecyclableCache;

public class NullCache<T extends Recyclable>
	implements RecyclableCache<T>
{
	private List<T> list = new ArrayList<T>();
	
	@Override
	public T next()
	{
		return null;
	}

	@Override
	public void using(T t)
	{
		list.add(t);
	}

	@Override
	public void dispose()
	{}

	@Override
	public CacheStatus status()
	{
		return new NullCacheSatus(list);
	}

	@Override
	public Iterator<T> iterator()
	{
		return null;
	}

	@Override
	public void vaccum()
	{}
}

class NullCacheSatus<T>
	implements CacheStatus
{
	private final List<T>	list;

	public NullCacheSatus(List<T> list)
	{
		this.list = list;
	}

	@Override
	public int available()
	{
		return 0;
	}

	@Override
	public int taken()
	{
		return list.size();
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Override
	public int capacity()
	{
		return list.size();
	}
}