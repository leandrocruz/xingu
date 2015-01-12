package xingu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xingu.utils.clone.CloneException;
import xingu.utils.reflect.FieldHandler;
import xingu.utils.reflect.impl.BooleanFieldHandler;
import xingu.utils.reflect.impl.ByteFieldHandler;
import xingu.utils.reflect.impl.CharacterFieldHandler;
import xingu.utils.reflect.impl.DoubleFieldHandler;
import xingu.utils.reflect.impl.FloatFieldHandler;
import xingu.utils.reflect.impl.IntegerFieldHandler;
import xingu.utils.reflect.impl.LongFieldHandler;
import xingu.utils.reflect.impl.MissingFieldHandler;
import xingu.utils.reflect.impl.ShortFieldHandler;

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

    public static void setField(Object bean, String fieldName, Object value)
        throws Exception
    {
    	Field field = getField(bean.getClass(), fieldName);
    	setField(bean, field, value);
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

	public static void copyValue(Field field, Object source, Object copy)
	{
		Object value = valueFrom(field, source);
		set(field, copy, value);
	}

	public static void copyPrimitive(Field field, Object src, Object copy)
	{
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		try
		{
			handlerFor(field).transferValue(field, src, copy);
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
	
	public static FieldHandler handlerFor(Field field)
	{
		Type type = field.getType();
		if (Boolean.TYPE.equals(type))
		{
			return BooleanFieldHandler.instance();
		}
		else if (Byte.TYPE.equals(type))
		{
			return ByteFieldHandler.instance();
		}
		else if (Character.TYPE.equals(type))
		{
			return CharacterFieldHandler.instance();
		}
		else if (Short.TYPE.equals(type))
		{
			return ShortFieldHandler.instance();
		}
		else if (Integer.TYPE.equals(type))
		{
			return IntegerFieldHandler.instance();
		}
		else if (Long.TYPE.equals(type))
		{
			return LongFieldHandler.instance();
		}
		else if (Float.TYPE.equals(type))
		{
			return FloatFieldHandler.instance();
		}
		else if (Double.TYPE.equals(type))
		{
			return DoubleFieldHandler.instance();
		}
		
		return MissingFieldHandler.instance();
	}
}