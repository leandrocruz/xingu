package br.com.ibnetwork.xingu.utils;

import junit.framework.TestCase;

public class ArgsUtilsTest
    extends TestCase
{
    public void testNormalize()
        throws Exception
    {
    	String[] expected = new String[]{
    			"a",
    			"b c d",
    			"e"
    	};

    	String[] array = new String[]{
    			"a",
    			"'b",
    			"c",
    			"d'",
    			"e"
    	};

    	array = ArgsUtils.norm(array);
        assertTrue(ArrayUtils.equals(expected, array));
    }
}
