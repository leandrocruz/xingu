package br.com.ibnetwork.xingu.utils.cache;

import java.util.Arrays;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class RecyclableCache<T extends Recyclable>
{
	private int capacity;
	
	private int ic; // items count
	
	private Pointer[] array;
	
	public RecyclableCache()
	{
		this(20);
	}
	
	public RecyclableCache(int size)
	{
		this.capacity = size;
		array = new Pointer[size];
	}
	
	@SuppressWarnings("unchecked")
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
	
	public void returnItem(T item)
	{
		for (int i = 0; i < capacity; i++)
		{
			Pointer p = array[i];
			if(p != null && p.item == item)
			{
				p.available = true;
				item.reclycle();
				return;
			}
		}
		throw new NotImplementedYet("Item '" + item + "' not add to cache");
	}

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