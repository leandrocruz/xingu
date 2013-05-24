package br.com.ibnetwork.xingu.utils.cache;

import java.util.Iterator;

public interface RecyclableCache<T extends Recyclable>
{
	T next();

	void using(T t);

	void dispose();

	CacheStatus status();

	Iterator<T> iterator();

	void vaccum();
}