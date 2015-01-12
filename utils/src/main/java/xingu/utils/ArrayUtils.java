package xingu.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class ArrayUtils
{
	public static final Object toArrayOf(Collection<?> collection, Class<?> typeOfArray)
	{
		int    size  = collection.size();
		Object array = Array.newInstance(typeOfArray, size);

		int i = 0;
		for(Object item : collection)
		{
			Array.set(array, i++, item);
		}
		
		return array;
	}

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
        int      oldSize        = Array.getLength(oldArray);
        Class<?> elementType    = oldArray.getClass().getComponentType();
        Object   newArray       = Array.newInstance(elementType, newSize);
        int      preserveLength = Math.min(oldSize, newSize);
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
		return toString(array, separator, null, false, null, false);
	}

	public static String toString(Object[] array, String separator,String valueIfNull, String printNull, String valueIfEmpty, String printEmpty)
	{
		boolean printWhenNull  = BooleanUtils.toBoolean(printNull);
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
			result = StringUtils.removeEnd(result,separator);
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


    public static List<String> toList(String... array)
    {
    	List<String> result = new ArrayList<String>(array.length);
    	for(String s : array)
    	{
    		result.add(s);
    	}
    	return result;
    }

    public static List<Integer> toList(Integer... array)
    {
    	List<Integer> result = new ArrayList<Integer>(array.length);
    	for(Integer i : array)
    	{
    		result.add(i);
    	}
    	return result;
    }
        
    public static final boolean equals(Object expected, Object array)
    {
    	if(expected == null && array == null)
    	{
    		return true;
    	}
    	
    	int expectedLength = Array.getLength(expected);
    	int arrayLength = Array.getLength(array);
    	
    	if(expectedLength != arrayLength)
    	{
    		return false;
    	}
    	
    	for(int i = 0; i < expectedLength; i++)
    	{
    		Object a = Array.get(expected, i);
    		Object b = Array.get(array, i);
    		if(!a.equals(b))
    		{
    			return false;
    		}
    	}
    	return true;
    }

	public static String[] merge(String[] a, String[] b)
	{
		String[] result = new String[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
