package br.com.ibnetwork.xingu.utils;

public class NumberUtils
{
    public static int toInt(String value, int defaultValue)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch(Throwable t)
        {
            return defaultValue;
        }
    }

    public static long toLong(String value, long defaultValue)
    {
    	value = StringUtils.trimToNull(value);
    	if(value == null)
    	{
    		return defaultValue;
    	}
    	
        try
        {
            return Long.parseLong(value);
        }
        catch(Throwable t)
        {
            return defaultValue;
        }
    }

}
