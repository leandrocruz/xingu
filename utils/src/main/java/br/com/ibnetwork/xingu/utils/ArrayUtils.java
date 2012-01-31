package br.com.ibnetwork.xingu.utils;

import java.lang.reflect.Array;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public class ArrayUtils
{
	public static String[] toStringArray(Object[] array)
	{
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++)
        {
            Object object = array[i];
            String value = object.toString();
            result[i] = value;
        }
        return result;
	}
	
    public static Object resizeArray(Object oldArray, int newSize)
    {
        int oldSize = Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
        {
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

	public static String toString(long[] array, String separator)
	{
		Object[] newArray = new Object[array.length];
		for (int i = 0; i < array.length; i++)
        {
            long l = array[i];
            newArray[i] = new Long(l);
        }
        return toString(newArray,separator);
	}

	public static String toString(Object[] array, String separator)
	{
		return toString(array,separator,null,false,null,false);
	}
	
	public static String toString(Object[] array, String separator,String valueIfNull, String printNull, String valueIfEmpty, String printEmpty)
	{
		boolean printWhenNull = BooleanUtils.toBoolean(printNull);
		boolean printWhenEmpty = BooleanUtils.toBoolean(printEmpty);
		return toString(array,separator,valueIfNull,printWhenNull,valueIfEmpty,printWhenEmpty);
	}


	public static String toString(Object[] array, String separator,String valueIfNull, boolean printWhenNull, String valueIfEmpty, boolean printWhenEmpty)
	{
		StringBuffer buffer = new StringBuffer();
		int size = array.length;
		for (int i = 0; i < array.length; i++)
		{
			boolean appended = false;
			Object obj = array[i];
			if(obj != null)
			{
				String string = obj.toString();
				if(StringUtils.isNotEmpty(string))
				{
					buffer.append(obj.toString());
					appended = true;	
				}
				else if(printWhenEmpty)
				{
					buffer.append(valueIfEmpty);
					appended = true;
				}
			}
			else if(printWhenNull)
			{
				buffer.append(valueIfNull);
				appended = true;
			}
            
			if(i < size - 1 && appended)
			{
				buffer.append(separator);
			}
		}
		String result = buffer.toString();
		if(result.endsWith(separator))
		{
			result = StringUtils.chompLast(result,separator);
		}
		return result;
	}

	public static int countNulls(Object[] array)
	{
		if(array == null)
		{
			return 0;
		}
		int nullCount = 0;
		for (int i = 0; i < array.length; i++) 
		{
			Object object = array[i];
			if(object == null)
			{
				nullCount++;
			}
		}
		return nullCount;
	}
    
    public static Object[] replaceNulls(Object[] array, Object replacement)
    {
        if(array == null)
        {
            return null;
        }
        for (int i = 0; i < array.length; i++)
        {
            Object obj = array[i];
            if(obj == null)
            {
                array[i] = replacement;
            }
        }
        return array;
    }
}
