package br.com.ibnetwork.xingu.utils.cache;

public interface CacheStatus
{
	int available();

	int taken();
	
	int size();
	
	int capacity();
}
