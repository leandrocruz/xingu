package br.com.ibnetwork.xingu.utils;

import java.nio.charset.Charset;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtils
{
    public static final String EMPTY = "";
    
    public static final String SPACE = " ";
    
    public static final String SLASH = "/";
    
    public static final String BACK_SLASH = "\\";
    
    public static final String QUESTION_MARK = "?";
    
    public static final String DOT = ".";

    public static final String EQUALS = "=";

    public static final String AND = "&";
    
    public static final Charset UTF8 = Charset.forName("utf8");

    public static final boolean isEmpty(String[] array)
    {
        return array == null || array.length == 0;
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

	public static String toHex(byte bytes[]) 
    {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;
        
        for (i = 0; i < bytes.length; i++) 
        {
            if ((bytes[i] & 0xff) < 0x10) 
            {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString();
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

}
