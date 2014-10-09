package br.com.ibnetwork.xingu.utils.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FluidMap<T>
{
	private Map<String, List<T>> map;
	
	public FluidMap()
	{
		this.map = new HashMap<String, List<T>>();
	}

	public FluidMap(Map<String, T> values)
	{
		this.map = new HashMap<String, List<T>>(values.size());
		Set<String> keys = values.keySet();
		for(String key : keys)
		{
			T value = values.get(key);
			add(key, value);
		}
	}

	private List<T> safeGet(String key)
	{
		List<T> list = map.get(key);
		if(list == null)
		{
			list = new ArrayList<T>();
			map.put(key, list);
		}
		return list;
	}

	public FluidMap<T> add(String key, T value)
	{
		List<T> list = safeGet(key);
		list.add(value);
		return this;
	}
	public FluidMap<T> add(String key, @SuppressWarnings("unchecked") T... values)
	{
		List<T> list = safeGet(key);
		for(T value : values)
		{
			list.add(value);
		}
		return this;
	}

	public List<T> getAll(String key)
	{
		return map.get(key);
	}

	public T get(String key)
	{
		if(!hasValueFor(key))
		{
			return null;
		}
		List<T> list = getAll(key);
		return list.get(0);
	}

	public boolean hasValueFor(String key)
	{
		List<T> list = map.get(key);
		return list == null ? false : list.size() > 0;
	}

	public int size()
	{
		return map.keySet().size();
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public boolean subsetOf(FluidMap<T> other)
	{
		if(isEmpty())
		{
			return other.isEmpty();
		}

		Set<String> keys = map.keySet();
		for(String key : keys)
		{
			List<T> list = getAll(key);
			List<T> otherList = other.getAll(key);
			boolean match = list == null ? otherList == null : false || list != null ? otherList != null : false;  
			if(!match)
			{
				return false;
			}

			for(T t : list)
			{
				boolean contains = otherList.contains(t);
				if(!contains)
				{
					return false;
				}
			}
		}
		return true;
	}

	public Set<String> keySet()
	{
		return map.keySet();
	}

	@Override
	public String toString()
	{
		return map.toString();
	}

	public void add(FluidMap<T> map)
	{
		if(map != null && !map.isEmpty())
		{
			Set<String> keys = map.keySet();
			for(String key : keys)
			{
				List<T> values = map.getAll(key);
				for(T t : values)
				{
					add(key, t);
				}
			}
		}
	}

	public FluidMap<T> copy()
	{
		FluidMap<T> copy = new FluidMap<T>();
		if(map == null || map.isEmpty())
		{
			return copy;
		}
		copy.add(this);
		
		return copy;
	}
}