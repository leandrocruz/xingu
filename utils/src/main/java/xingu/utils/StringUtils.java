package xingu.utils;

import java.nio.charset.Charset;
import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class StringUtils
{
	public static final String		EMPTY			= "";

	public static final String		SPACE			= " ";

	public static final String		SLASH			= "/";

	public static final String		BACK_SLASH		= "\\";

	public static final String		QUESTION_MARK	= "?";

	public static final String		DOT				= ".";

	public static final String		EQUALS			= "=";

	public static final String		AND				= "&";

	public static final Charset		UTF8			= Charset.forName("utf8");

	public static final String[]	EMPTY_ARRAY		= new String[] {};

	public static final String[]	UNITS			= new String[] {"", "K", "M", "G", "T", "P", "E"};
    
    public static final boolean isEmpty(String[] array)
    {
        return array == null || array.length == 0;
    }

    public static final boolean upperCaseStart(String input)
    {
    	if(input == null)
    	{
    		throw new IllegalArgumentException("Input is null");
    	}
    	if(input.length() == 0)
    	{
    		return false;
    	}
    	char[] array = input.toCharArray();
    	char c = array[0];
    	return Character.isUpperCase(c);
    }

    public static final boolean isEmpty(Collection<String> coll)
    {
        return coll == null || coll.isEmpty();
    }

    public static boolean splitAndFindIgnoreCase(String wanted, String expression)
    {
        return splitAndFindIgnoreCase(wanted, expression, SPACE);
    }
    
    public static boolean splitAndFindIgnoreCase(String foo, String expression, String separator)
    {
        if(isEmpty(foo) || isEmpty(expression) || isEmpty(separator))
        {
            return false;
        }

        String[] splited = expression.split(separator);
        return findIgnoreCase(foo, splited);
    }

    public static boolean findIgnoreCase(String foo, String[] array)
    {
        return findWithCollatorOrIgnoreCase(foo, array, null);
    }

    public static boolean findWithCollatorOrIgnoreCase(String foo, String[] array, Collator collator)
    {
        if(isEmpty(foo) || isEmpty(array))
        {
            return false;
        }
        
        for(String item : array)
        {
            if(collator != null)
            {
                if(collator.compare(foo, item) == 0)
                {
                    return true;
                }
            }
            else if(foo.equalsIgnoreCase(item))
            {
                return true;
            }
        }
        
        return false;

    }
    
    public static boolean findIgnoreCase(String foo, Collection<String> coll)
    {
        return findIgnoreCase(foo, coll, null);
    }

    public static boolean findIgnoreCase(String foo, Collection<String> coll, Collator collator)
    {
        if(isEmpty(foo) || isEmpty(coll))
        {
            return false;
        }
        
        for(String item : coll)
        {
            if(collator != null)
            {
                if(collator.compare(foo, item) == 0)
                {
                    return true;
                }
            }
            else if(foo.equalsIgnoreCase(item))
            {
                return true;
            }
        }
        
        return false;
    }

    
    public static String joinIgnoringNulls(String[] parts, String separator)
    {
        StringBuffer sb = new StringBuffer();
        int size = parts.length;
        String last = null;
        for (int i = 0; i < size; i++)
        {
            String part = parts[i];
            String next = i + 1 < size ? parts[i + 1] : null;
            if(part != null)
            {
                if(last != null && !last.equals(separator))
                {
                    sb.append(separator);
                }
                sb.append(part);
                last = part;
                if(next != null)
                {
                    sb.append(separator);
                    last = separator;
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    public static final boolean isValid(String foo)
    {
        return (foo != null && foo.length() > 0);
    }

    public static final boolean isEmpty(String foo)
    {
        return (foo == null || foo.trim().length() == 0);
    }

    public static boolean isNotEmpty(String s)
    {
        return !isEmpty(s);
    }

    public static final boolean isEmptyOrEquals(String foo, String... candidates)
    {
    	if(StringUtils.isEmpty(foo))
        {
        	return true;
        }
    	for (String bar : candidates)
		{
			if(foo.equals(bar))
			{
				return true;
			}
		}
    	return false;
    }

    public static final String makeString(String foo)
    {
        return (foo == null ? EMPTY : foo.trim());
    }
    
    public static final String firstLetterCaps(String data)
    {
		String firstLetter = data.substring(0,1).toUpperCase();
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
    }

	public static final String firstLetterLowerCase(String data)
	{
		String firstLetter = data.substring(0,1).toLowerCase();
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
	}

    public static String orEmpty(String s)
    {
        return s != null ? s : EMPTY;
    }

    public static String trimToNull(String str) 
    {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trim(String str) 
    {
    	return str == null ? null : str.trim();
    }

    public static String flat(String str)
    {
    	if(str == null)
    	{
    		return null;
    	}
    	
		String normalized = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		int len = normalized.length();
		StringBuffer sb = new StringBuffer();
		
		int spaceCount = 0;
		
		for (int i = 0; i < len;)
		{
			int     codePoint  = normalized.codePointAt(i);
			boolean whitespace = Character.isWhitespace(codePoint);
			boolean space      = Character.isSpaceChar(codePoint);
			boolean alphabetic = Character.isAlphabetic(codePoint);
			int     offset     = Character.charCount(codePoint);
			/*
			String  name       = Character.getName(codePoint);
			int     type       = Character.getType(codePoint);
			System.out.println("at: " + i + " type: " + type + " offset: " + offset + " name: '" + name + "'");
			*/
			
			i += offset;
			if(alphabetic)
			{
				spaceCount = 0;
				char[] chars = Character.toChars(codePoint);
				sb.append(chars);
			}
			else if(space || whitespace)
			{
				spaceCount++;
				if(spaceCount == 1)
				{
					sb.append(SPACE);
				}
			}
			else
			{
				
			}
		}
		return sb.toString().trim();
    }

    public static String normalizeWhiteSpace(String str)
    {
    	if(str == null)
    	{
    		return null;
    	}
    	
		String normalized = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		int len = normalized.length();
		StringBuffer sb = new StringBuffer();
		
		int spaceCount = 0;
		
		for (int i = 0; i < len;)
		{
			int     codePoint  = normalized.codePointAt(i);
			boolean whitespace = Character.isWhitespace(codePoint);
			boolean space      = Character.isSpaceChar(codePoint);
			int     offset     = Character.charCount(codePoint);
			
			i += offset;
			if(space || whitespace)
			{
				spaceCount++;
				if(spaceCount == 1)
				{
					sb.append(SPACE);
				}
			}
			else
			{
				spaceCount = 0;
				char[] chars = Character.toChars(codePoint);
				sb.append(chars);
			}
		}
		
		String result = sb.toString().trim();
		return Normalizer.normalize(result, java.text.Normalizer.Form.NFC);
    }

    public static int toInt(String str)
    {
        return toInt(str, 0);
    }
    
    public static int toInt(String str, int onError)
    {
        if(isEmpty(str))
        {
            return onError;
        }
        try
        {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e)
        {
            return onError;
        }
    }

    public static boolean findAnyOfWithCollator(String[] anyOf, String[] input, Collator collator)
    {
        return findAnyOfWithCollator(anyOf, input, collator, StringUtils.SPACE);
    }
    
    /*
     * Finds any occurrence of nested inside input
     * Consider nested[] is: 'car', 'yellow house'
     */
    public static boolean findAnyOfWithCollator(String[] anyOf, String[] input, Collator collator, String separator /* applied to nested when required */)
    {
        if(input.length == 1)
        {
            /* no need to split blocked */
            return StringUtils.findWithCollatorOrIgnoreCase(input[0], anyOf, collator);
        }
        
        List<String[]> parts = split(anyOf, separator);
        for (String[] part : parts)
        {
            if(part.length == 1 
                    && StringUtils.findWithCollatorOrIgnoreCase(part[0], input, collator))
            {
                /* works for 'car' */
                return true;
            }
            else if(part.length > 1
                    && StringUtils.indexOf(part, input, collator) >= 0)
            {
                /* works for 'yellow house' */
                return true;
            }
        }
        return false;
    }

    public static List<String[]> split(String[] array, String separator)
    {
        List<String[]> result = new ArrayList<String[]>();
        for(String s : array)
        {
            result.add(s.split(separator));
        }
        return result;
    }

    public static int indexOf(String[] smaller, String[] bigger, Collator collator)
    {
        if(smaller.length > bigger.length)
        {
            return -1;
        }
        for(int i = 0 ; i < bigger.length ; i++)
        {
            if(foundAt(smaller, bigger, collator, i))
            {
                return i;
            }
        }
        return -1;
    }

    private static boolean foundAt(String[] smaller, String[] bigger, Collator collator, int offset)
    {
        if(smaller.length + offset > bigger.length)
        {
            return false;
        }
        
        int j = 0;
        for(int i = offset ; i < bigger.length ; i++)
        {
            if(j + 1 > smaller.length)
            {
                return true;
            }
            String fromBigger = bigger[i];
            String fromSmaller = smaller[j];
            if(collator.compare(fromBigger, fromSmaller) != 0)
            {
                return false;
            }
            j++;
            continue;
        }
            
        return true;
    }

    private static int indexOfOrig(String[] smaller, String[] bigger, Collator collator)
    {
        if(smaller.length > bigger.length)
        {
            return -1;
        }
        int offset = -1;
        int count = 0;  
        for(int i = 0 ; i < smaller.length ; i++)
        {
            //offset = -1;
            String sm = smaller[i];
            for(int j = offset+1 ; j < bigger.length ; j++)
            {
                String bi = bigger[j];
                if(collator.compare(sm, bi) == 0)
                {
                    offset = j;
                    count++;
                    break;
                }
                else
                {
                    if(offset >= 0)
                    {
                        return -1;
                    }
                }
            }
        }
        if(count == smaller.length)
        {
            /* We found all of our tokens */
            return offset;
        }
        return -1;
    }

    public static String toPercent(double value)
    {
    	return toPercent(value * 100.0, 100.0);
    }
    
    public static String toPercent(double value, double total)
    {
        double percent = value/total;
        percent = percent * 100;
        String result = String.valueOf(percent);
        result = result.substring(0, result.indexOf(".") + 2);
        return result + "%";
    }

	public static String replaceOn(String replaceMe, String src, String replacement)
	{
		int start = src.indexOf(replaceMe);
		int end = start + replaceMe.length();
		String result = src.substring(0, start) + replacement + src.substring(end);
		return result;
	}

	public static final String toColumn(String input, int width)
	{
		int size  = input.length();
		int start = 0;
		int end   = width;
		
		StringBuffer sb = new StringBuffer();
		do
		{
			String line = input.substring(start, end);
			sb.append(line).append("\n");
			start       = end;
			end         = end + width;
			end         = end < size ? end : size;
		}
		while(start < size);
		return sb.toString();
	}

	public static String normalizePath(String path)
	{
		char[]       array = path.toCharArray();
		StringBuffer sb    = new StringBuffer();
		
		boolean skipNextPathChar = false;
		int     idx  = 0;

		for(int i = 0; i < array.length; i++)
		{
			char c = array[i];
			boolean isPathChar = isPathChar(c);
			if(isPathChar)
			{
				if(skipNextPathChar)
					continue;

				skipNextPathChar = true;
			}
			else
			{
				skipNextPathChar = false;
			}
			
			idx++;
			sb.append(c);
		}

		if(idx > 1)
		{
			char last = sb.charAt(idx - 1);
			if(isPathChar(last))
			{
				return sb.substring(0, idx - 1);
			}
		}

		return sb.toString();
	}

	public static boolean isPathChar(char c)
	{
		return c == '/' || c == '.';
	}

	public static final String onlyDigits(String input)
	{
		return input.replaceAll("[^0-9]", "");
	}
	
	public static String unitFormat(long count)
	{
		return unitFormat(count, UNITS);
	}

	public static String unitFormat(long count, String[] units)
	{
		return unitFormat(count, units, Locale.ENGLISH);
	}

	public static String unitFormat(long count, String[] units, Locale locale)
	{
		if(count < 0)
		{
			return "neg!";
		}

		int power = 0;
		while(true)
		{
	        double min = power == 0 ? 0 : Math.pow(1000, power);
	        double max = Math.pow(1000, power + 1);
	        if(count >= min && count < max)
	        {
	        	double value = power == 0 ? count : count/min;
	        	return String.format(locale, "%3.1f %s", value, units[power]);
	        }
	        power++;
		}
	}

	public static boolean anyNull(String... values)
	{
		if(values == null)
		{
			return true;
		}
		
		for(String value : values)
		{
			if(value == null)
			{
				return true;
			}
		}
		return false;
	}
}
