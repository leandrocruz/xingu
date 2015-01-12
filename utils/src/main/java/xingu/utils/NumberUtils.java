package xingu.utils;

import java.util.Random;

import xingu.lang.NotImplementedYet;

public class NumberUtils
{
	private static Random rnd = new Random(System.currentTimeMillis());

	/*
	 * Generates a random number between start/end
	 */
	public static long rnd(int start, int end)
	{
		int diff = end - start;
		return start + rnd.nextInt(diff + 1);
	}
	
	public static String strip(String string)
	{
		return string.replaceAll("[^\\d]", "");
	}

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

    public static final int divRoundUp(int a, int b)
    {
    	if(b > a)
    	{
    		return 1;
    	}
		int result = a % b;
		return result == 0 ? a / b : (a - result) / b + 1;
    }

	public static final int atoi(String s)
	{
		if(StringUtils.isEmpty(s))
		{
			throw new NotImplementedYet("Emty String");
		}

		int signal = 1;
		int result = 0;
		int pow    = 0;

		char[] array = s.toCharArray();
		int    start = array.length - 1;
		for(int i = start ; i >= 0; i--)
		{
			char c = array[i];
			boolean isDigit = Character.isDigit(c);
			if(isDigit)
			{
				int digit  = Character.digit(c, 10);
				int factor = (int) Math.pow(10.0, pow++);
				result += digit * factor; 
			}
			else if(c == '-')
			{
				signal = -1;
			}
			else
			{
				throw new NotImplementedYet("Not a number: " + s);
			}
		}
		return signal * result;
	}
}