package br.com.ibnetwork.xingu.utils;

import junit.framework.TestCase;

public class JdbcUtilsTest
	extends TestCase
{
	public void testColumName()
		throws Exception
	{
		assertEquals("SAMPLE", JdbcUtils.toColumnName("sample"));
		assertEquals("SAMPLE_DATE", JdbcUtils.toColumnName("sampleDate"));
		assertEquals("SAMPLE_DATE_XY", JdbcUtils.toColumnName("sampleDateXy"));
		assertEquals("SAMPLE_DATE_X", JdbcUtils.toColumnName("sampleDateX"));
	}
}
