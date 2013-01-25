package br.com.ibnetwork.xingu.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class CollectionUtils
{
	private static final Integer ONE = new Integer(1);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Collection<?> EMPTY_COLLECTION = Collections.unmodifiableCollection(new ArrayList());

	public static <T> void forAllDo(final Collection<T> collection, Closure<T> closure)
	{
		if (collection != null && closure != null)
		{
			for (T t : collection)
			{
				closure.execute(t);
			}
		}
	}

	public static <T> Collection<T> copy(final Collection<T> col)
	{
		if(col == EMPTY_COLLECTION)
		{
			return col;
		}

		if (col instanceof ArrayList)
		{
			return new ArrayList<T>(col);
		}
		else if (col instanceof LinkedList)
		{
			return new LinkedList<T>(col);
		}
		else
		{
			throw new NotImplementedYet("Collection type '" + col.getClass().getName() + "' not supported");
		}
	}

	public static <T> Collection<T> subtract(final Collection<T> a, final Collection<T> b)
	{
		Collection<T> result = copy(a);
		Iterator<T> it = b.iterator();
		while (it.hasNext())
		{
			T t = it.next();
			result.remove(t);
		}
		return result;
	}

	public static <T> Map<T, Integer> getCardinalityMap(final Collection<T> coll)
	{
		if(coll == null)
		{
			return null;
		}

		Map<T, Integer> result = new HashMap<T, Integer>();
		Iterator<T> it = coll.iterator();
		while (it.hasNext())
		{
			T t = it.next();
			Integer count = result.get(t);
			if (count == null)
			{
				result.put(t, ONE);
			}
			else
			{
				result.put(t, new Integer(count.intValue() + 1));
			}
		}
		return result;
	}

	private static final <T> int getFreq(final T obj, final Map<T, Integer> freqMap)
	{
		Integer count = freqMap.get(obj);
		if (count != null)
		{
			return count.intValue();
		}
		return 0;
	}

	public static <T> Collection<T> intersection(final Collection<T> a, final Collection<T> b)
	{
		if(a == null || a.size() == 0 || b == null || b.size() == 0)
		{
			return null;
		}

		List<T> result = new ArrayList<T>();
		Map<T, Integer> mapa = getCardinalityMap(a);
		Map<T, Integer> mapb = getCardinalityMap(b);

		Set<T> elts = new HashSet<T>(a);
		elts.addAll(b);
		Iterator<T> it = elts.iterator();
		while (it.hasNext())
		{
			T t = it.next();
			for (int i = 0, m = Math.min(getFreq(t, mapa), getFreq(t, mapb)); i < m; i++)
			{
				result.add(t);
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> CollectionDifference<T> asymmetricDifference(Collection<T> a, Collection<T> b)
	{
		Collection<T> inter = CollectionUtils.intersection(a, b);
		if(inter != null && inter.size() > 0)
		{
			a = subtract(a, inter);
			b = subtract(b, inter);
		}
		return new CollectionDifference(inter, a, b);
	}
}