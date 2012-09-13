package br.com.ibnetwork.xingu.utils.cache;

public interface CacheStatus
{
	int cached();

	int taken();
	
	int size();
	
	int capacity();
}
