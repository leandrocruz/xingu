package br.com.ibnetwork.xingu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.clone.CloneException;

public class FieldUtils
{
	public static List<Field> getAllFields(Class<?> clazz)
	{
		List<Field> result = new ArrayList<Field>();
		do
		{
			collect(clazz, result); 
			clazz = clazz.getSuperclass();	
		}
		while(clazz != null);
		return result;
	}

	private static void collect(Class<?> clazz, List<Field> result)
    {
		Field[] declared = clazz.getDeclaredFields();
		for (Field field : declared)
        {
	        result.add(field);
        }
    }
	
	public static Field getField(Class<?> clazz,String name)
	{
		List<Field> allFields = getAllFields(clazz);
		for (Field field : allFields)
        {
	        if(field.getName().equals(name))
	        {
	        	return field;
	        }
        }
		return null;
	}

    public static void setField(Object bean, Field field, Object value)
        throws Exception
    {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(bean, value);
        field.setAccessible(accessible);
    }
    
    public static Object valueFrom(Field field, Object object)
	{
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object result = null;
        
		try
		{
			result = field.get(object);
		}
		catch (Throwable t)
		{
			throw new CloneException("Error retrieving value from field '" + field + "'", t);
		}
		finally
		{
			field.setAccessible(accessible);
		}
		
		return result;
	}

	public static void set(Field field, Object object, Object value)
	{
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

		try
		{
			field.set(object, value);
		}
		catch (Throwable t)
		{
			throw new CloneException("Error setting value on field '" + field + "'", t);
		}
		finally
		{
			field.setAccessible(accessible);
		}
	}

	public static void copyPrimitive(Field field, Object src, Object copy)
	{
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		try
		{
			Type type = field.getType();
			if (Boolean.TYPE.equals(type))
			{
				boolean b = field.getBoolean(src);
				field.setBoolean(copy, b);
			}
			else if (Byte.TYPE.equals(type))
			{
				byte b = field.getByte(src);
				field.setByte(copy, b);
			}
			else if (Character.TYPE.equals(type))
			{
				char c = field.getChar(src);
				field.setChar(copy, c);
			}
			else if (Short.TYPE.equals(type))
			{
				short s = field.getShort(src);
				field.setShort(copy, s);
			}
			else if (Integer.TYPE.equals(type))
			{
				int i = field.getInt(src);
				field.setInt(copy, i);
			}
			else if (Long.TYPE.equals(type))
			{
				long l = field.getLong(src);
				field.setLong(copy, l);
			}
			else if (Float.TYPE.equals(type))
			{
				float f = field.getFloat(src);
				field.setFloat(copy, f);
			}
			else if (Double.TYPE.equals(type))
			{
				double d = field.getDouble(src);
				field.setDouble(copy, d);
			}
		}
		catch (Exception e)
		{
			throw new CloneException("Error handling primitive field '" + field + "'", e);
		}
		finally
		{
			field.setAccessible(accessible);
		}
	}

}
