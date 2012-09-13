package br.com.ibnetwork.xingu.utils.cache;

public interface Recyclable
{
	void reclycle();
	
	boolean isReclycled();
	
	void markTaken();
}

