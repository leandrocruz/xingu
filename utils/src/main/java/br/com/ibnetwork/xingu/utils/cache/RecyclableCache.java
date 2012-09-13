package br.com.ibnetwork.xingu.utils.cache;

public interface RecyclableCache<T extends Recyclable>
{
	T next();

	void returnItem(T t);

	void using(T t);

	void dispose();

	CacheStatus status();
}