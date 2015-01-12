package xingu.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Ignore;
import org.junit.Test;

import xingu.utils.cache.Recyclable;
import xingu.utils.cache.RecyclableCache;
import xingu.utils.cache.impl.RecyclableCacheImpl;

@Ignore
public class RecyclableCacheTest
{
	@Test
	public void testCache()
		throws Exception
	{
		RecyclableCache<X> cache = new RecyclableCacheImpl<X>(3);
		X x = cache.next();
		assertNull(x);
		
		x = new X();
		cache.using(x);
		X x1 = cache.next();
		assertNull(x1);
		cache.vaccum();

		X x2 = cache.next();
		assertEquals(x, x2);
	}

	@Test
	public void testEnlargement()
		throws Exception
	{
		RecyclableCache<X> cache = new RecyclableCacheImpl<X>(1);
		X x1 = new X();
		X x2 = new X();
		X x3 = new X();
		X x4 = new X();
		
		cache.using(x1);
		cache.using(x2);
		cache.using(x3);
		cache.using(x4);

		X n1 = cache.next();
		X n2 = cache.next();
		X n3 = cache.next();
		X n4 = cache.next();
	
		assertEquals(null, n1);
		assertEquals(null, n2);
		assertEquals(null, n3);
		assertEquals(null, n4);
		
		returnItem(cache, x2);
		returnItem(cache, x4);
		returnItem(cache, x1);
		returnItem(cache, x3);
		
		X x5 = cache.next();
		assertEquals(null, x5);
		
		x5 = new X();
		cache.using(x5);
		cache.vaccum();
		
		X x6 = cache.next();
		assertSame(x5, x6);
	}

	private void returnItem(RecyclableCache<X> cache, X item)
	{
		cache.vaccum();
		X cached = cache.next();
		assertSame(item, cached);
	}
}

class X
	implements Recyclable
{
	private boolean recycled = false;
	
	@Override
	public boolean reclycle()
	{
		recycled = true;
		return true;
	}

	//@Override
	public void markTaken()
	{
		recycled = false;
	}

	@Override
	public String toString()
	{
		return "x (" + recycled + ")";
	}
}
