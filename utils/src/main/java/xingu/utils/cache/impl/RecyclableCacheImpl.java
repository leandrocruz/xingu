package xingu.utils.cache.impl;

import java.util.Arrays;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.lang.NotImplementedYet;
import xingu.utils.cache.CacheStatus;
import xingu.utils.cache.Recyclable;
import xingu.utils.cache.RecyclableCache;

public class RecyclableCacheImpl<T extends Recyclable>
	implements RecyclableCache<T>
{
	private int				capacity;

	private int				ic; // items count

	private Pointer<T>[]	array;

	private Logger			logger	= LoggerFactory.getLogger(getClass());
	
	public RecyclableCacheImpl()
	{
		this(20);
	}
	
	public RecyclableCacheImpl(int size)
	{
		this.capacity = size;
		array = new Pointer[size];
	}
	
	@Override
	public synchronized T next()
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer<T> p = array[i];
			if(p != null && p.available)
			{
				p.available = false;
				T item = p.item;
				return item;
			}
		}
		return null;
	}

	@Override
	public synchronized void using(T t)
	{
		ensureCapacity(ic + 1);
		array[ic] = new Pointer<T>(t);
		ic++;
	}

    private void ensureCapacity(int required)
    {
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

	@Override
	public synchronized void vaccum()
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer<T> p = array[i];
			if(p != null && !p.available)
			{
				T t = p.item;
				p.available = t.reclycle();
			}
		}
	}


	@Override
	public void dispose()
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer<T> p = array[i];
			if(p != null)
			{
				try
				{
					dispose(p.item);
				}
				catch (Exception e)
				{
					logger.warn("Error disposed cached item", e);
				}
			}
		}
	}

	protected void dispose(T t)
		throws Exception
	{}

	@Override
	public CacheStatus status()
	{
		int available = 0;
		int taken = 0;
		
		for (int i = 0; i < capacity; i++)
		{
			Pointer<T> p = array[i];
			if(p == null)
			{
				continue;
			}
			if(p.available)
			{
				available++;
			}
			else
			{
				//System.err.println(p.item);
				taken++;
			}
		}
		
		return new CacheStatusImpl(capacity, ic, available, taken);
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>(){

			int i = 0;
			
			@Override
			public boolean hasNext()
			{
				return i < capacity;
			}

			@Override
			public T next()
			{
				Pointer<T> pointer = array[i++];
				return pointer == null ? null : pointer.item;
			}

			@Override
			public void remove()
			{
				throw new NotImplementedYet();
			}
		};
	}
}

class CacheStatusImpl
	implements CacheStatus
{
	private final int	size;

	private final int	capacity;

	private final int	taken;

	private final int	available;	

	public CacheStatusImpl(int capacity, int size, int available, int taken)
	{
		this.capacity  = capacity;
		this.size      = size;
		this.available = available;
		this.taken     = taken;
	}

	@Override
	public int available()
	{
		return available;
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

	@Override
	public String toString()
	{
		return "capacity: '"+capacity+"', size: '"+size+"', available: '"+available+"', taken: '"+taken+"'";
	}
}

class Pointer<T>
{
	boolean available;
	
	final T item;

	public Pointer(T item)
	{
		this.item = item;
	}

	@Override
	public String toString()
	{
		return "available(" + available + ")";
	}
}