package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;
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
    
    @Test
    public void testStrip()
    {
    	String string = NumberUtils.strip("0_1.2,3+4/5%6^7*8(9)");
    	assertEquals("0123456789", string);
    }
    
    @Test
    public void testDivRoundUp()
    	throws Exception
    {
    	assertEquals(1,  NumberUtils.divRoundUp(1, 1));
    	assertEquals(1,  NumberUtils.divRoundUp(1, 2));
    	assertEquals(2,  NumberUtils.divRoundUp(2, 1));
    	assertEquals(20, NumberUtils.divRoundUp(20, 1));
    	assertEquals(10, NumberUtils.divRoundUp(20, 2));
    	assertEquals(7,  NumberUtils.divRoundUp(20, 3));
    	assertEquals(5,  NumberUtils.divRoundUp(20, 4));
    	assertEquals(4,  NumberUtils.divRoundUp(20, 5));
    	assertEquals(4,  NumberUtils.divRoundUp(20, 6));
    	assertEquals(3,  NumberUtils.divRoundUp(20, 7));
    	assertEquals(3,  NumberUtils.divRoundUp(20, 8));
    	assertEquals(3,  NumberUtils.divRoundUp(20, 9));
    	assertEquals(2,  NumberUtils.divRoundUp(20, 10));
    	assertEquals(1,  NumberUtils.divRoundUp(20, 21));
    }

    @Test
    public void testAtoi()
    	throws Exception
    {
    	assertEquals(1, NumberUtils.atoi("1"));
    	assertEquals(-1, NumberUtils.atoi("-1"));

    	assertEquals(10, NumberUtils.atoi("10"));
    	assertEquals(-10, NumberUtils.atoi("-10"));

    	assertEquals(101, NumberUtils.atoi("101"));
    	assertEquals(-101, NumberUtils.atoi("-101"));

    	assertEquals(Integer.MAX_VALUE, NumberUtils.atoi(String.valueOf(Integer.MAX_VALUE)));
    	assertEquals(Integer.MIN_VALUE, NumberUtils.atoi(String.valueOf(Integer.MIN_VALUE)));

//    	assertEquals(1, NumberUtils.atoi("1a"));
//    	assertEquals(-1, NumberUtils.atoi("-1a"));
//
//    	assertEquals(99, NumberUtils.atoi("9a9"));
//    	assertEquals(-99, NumberUtils.atoi("-9a9"));

    }
}