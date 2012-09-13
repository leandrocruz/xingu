package br.com.ibnetwork.xingu.utils.cache.impl;

import java.util.Arrays;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.cache.CacheStatus;
import br.com.ibnetwork.xingu.utils.cache.RecyclableCache;
import br.com.ibnetwork.xingu.utils.cache.Recyclable;

public class RecyclableCacheImpl<T extends Recyclable>
	implements RecyclableCache<T>
{
	private int capacity;
	
	private int ic; // items count
	
	private Pointer[] array;
	
	public RecyclableCacheImpl()
	{
		this(20);
	}
	
	public RecyclableCacheImpl(int size)
	{
		this.capacity = size;
		array = new Pointer[size];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized T next()
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer p = array[i];
			if(p != null && p.available)
			{
				p.available = false;
				T item = (T) p.item;
				item.markTaken();
				return item;
			}
		}
		return null;
	}
	
	@Override
	public void returnItem(T t)
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer p = array[i];
			if(p != null && p.item == t)
			{
				p.available = true;
				t.reclycle();
				return;
			}
		}
		throw new NotImplementedYet("Item '" + t + "' not add to cache");
	}

	@Override
	public void using(T t)
	{
		ensureCapacity(ic + 1);
		array[ic] = new Pointer(t);
		ic++;
	}
	
    private void ensureCapacity(int required) {
        if (required > capacity)
        {
            int size = (capacity * 3) / 2 + 1;
            if (size < required)
            {
            	size = required;
            }
            array = Arrays.copyOf(array, size);
            capacity = size;
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public void dispose()
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer p = array[i];
			if(p != null)
			{
				dispose((T) p.item);
			}
		}
	}

	protected void dispose(T t)
	{}

	@Override
	public CacheStatus status()
	{
		int inCache = 0;
		int taken = 0;
		
		return new CacheStatusImpl(capacity, ic, inCache, taken);
	}
}

class CacheStatusImpl
	implements CacheStatus
{
	private final int capacity;
	
	private final int size;
	
	private final int inCache;
	
	private final int taken;

	public CacheStatusImpl(int capacity, int size, int inCache, int taken)
	{
		this.capacity = capacity;
		this.size = size;
		this.inCache = inCache;
		this.taken = taken;
	}

	@Override
	public int cached()
	{
		return inCache;
	}

	@Override
	public int taken()
	{
		return taken;
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public int capacity()
	{
		return capacity;
	}
}

class Pointer
{
	boolean available;
	
	final Object item;

	public Pointer(Object item)
	{
		this.item = item;
	}

	@Override
	public String toString()
	{
		return "available(" + available + ")";
	}
}