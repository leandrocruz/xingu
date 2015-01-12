package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HexUtilsTest
{
	@Test
	public void testToHex()
	{
		assertEquals("20", HexUtils.toHex(32));
		assertEquals("FE", HexUtils.toHex(254));
		assertEquals("FF", HexUtils.toHex(255));
		assertEquals("3E7", HexUtils.toHex(999));
		assertEquals("7FFFFFFF", HexUtils.toHex(Integer.MAX_VALUE));
		assertEquals("80000000", HexUtils.toHex(Integer.MIN_VALUE));
	}
}
