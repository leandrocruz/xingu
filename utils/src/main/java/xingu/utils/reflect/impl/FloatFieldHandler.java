package xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import xingu.utils.reflect.FieldHandler;

public class FloatFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new FloatFieldHandler();
	
	private FloatFieldHandler()
	{}

	public static FieldHandler instance()
	{
		return instance;
	}

	@Override
	public boolean isPrimitive()
	{
		return true;
	}

	@Override
	public void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException
	{
		float f = field.getFloat(src);
		field.setFloat(dst, f);
	}

	@Override
	public String typeName()
	{
		return Float.TYPE.getName();
	}
}
