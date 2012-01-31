package br.com.ibnetwork.xingu.utils;

import junit.framework.TestCase;

public class ArrayUtilsTest
    extends TestCase
{
    public void testReplaceNulls()
        throws Exception
    {
        Object[] array = new Object[]{"1",null,new Integer(2)};
        array = ArrayUtils.replaceNulls(array,"NULL");
        assertEquals("1",array[0]);
        assertEquals("NULL",array[1]);
        assertEquals(new Integer(2),array[2]);
    }
}
