package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanUtilsTest
{
	@Test
	public void testToBoolean()
		throws Exception
	{
		assertEquals(true, BooleanUtils.toBoolean("1"));
		assertEquals(true, BooleanUtils.toBoolean("true"));
		assertEquals(true, BooleanUtils.toBoolean("y"));
		assertEquals(true, BooleanUtils.toBoolean("s"));
		assertEquals(true, BooleanUtils.toBoolean("yes"));
		assertEquals(true, BooleanUtils.toBoolean("sim"));

		assertEquals(false, BooleanUtils.toBoolean("n"));
		assertEquals(false, BooleanUtils.toBoolean("0"));
		assertEquals(false, BooleanUtils.toBoolean("false"));
	}
}