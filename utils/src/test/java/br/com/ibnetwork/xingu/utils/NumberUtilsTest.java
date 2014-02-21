package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberUtilsTest
{
    @Test
    public void testRandom()
        throws Exception
    {
    	int start = 0;
    	int end = 5;
    	
    	for(int i=0 ; i<1000 ; i++)
    	{
    		long rnd = NumberUtils.rnd(start, end);
    		assertTrue(rnd >= start);
    		assertTrue(rnd <= end);
    	}
    }
}
