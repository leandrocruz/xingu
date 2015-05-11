package xingu.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xingu.utils.TimeUtils;

public class TimeUtilsTest
{
    @Test
    public void testToMillis()
        throws Exception
    {
        assertEquals(-1, TimeUtils.toMillis(null));
        assertEquals(-1, TimeUtils.toMillis(""));
        assertEquals(0, TimeUtils.toMillis("0s"));
        assertEquals(1, TimeUtils.toMillis("1ms"));
        assertEquals(2000, TimeUtils.toMillis("2s"));
        assertEquals(60000, TimeUtils.toMillis("1m"));
        assertEquals(3600000, TimeUtils.toMillis("1h"));
        assertEquals(86400000, TimeUtils.toMillis("1d"));
    }

    @Test
    public void testToString()
    	throws Exception
    {
    	int millis = 100000;
		assertEquals("1.6666666666666667m", TimeUtils.toString(millis, "m", false));
    	assertEquals("1.67m", TimeUtils.toString(millis, "m", true));

		assertEquals("1666.6666666666667m", TimeUtils.toString(millis * 1000, "m", false));
    	assertEquals("1666.67m", TimeUtils.toString(millis * 1000, "m", true));
    }
}
