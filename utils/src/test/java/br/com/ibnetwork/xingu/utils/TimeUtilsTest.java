package br.com.ibnetwork.xingu.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
}
