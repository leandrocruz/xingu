package br.com.ibnetwork.xingu.utils.ip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IPUtilsTest
{
	@Test
	public void testIsValid()
		throws Exception
	{
		assertEquals(true, IPUtils.isValid("127.0.0.1"));
		assertEquals(true, IPUtils.isValid("10.0.0.1"));
		
		assertEquals(false, IPUtils.isValid("."));
		assertEquals(false, IPUtils.isValid(".."));
		assertEquals(false, IPUtils.isValid("..."));
		assertEquals(false, IPUtils.isValid("...."));
		assertEquals(false, IPUtils.isValid("127.0.0."));
		assertEquals(false, IPUtils.isValid("127..0.1"));
		assertEquals(false, IPUtils.isValid("127.0..1"));
	}

	@Test
	public void testParse()
		throws Exception
	{
		IPv4Address address = IPUtils.buildIPv4From("127.0.0.1");
		assertEquals(new IPv4Address(127, 0, 0, 1), address);
		assertEquals(0, new IPv4Address(127, 0, 0, 1).compareTo(address));
	}
}
