package xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import xingu.utils.reflect.FieldHandler;

public class BooleanFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new BooleanFieldHandler();
	
	private BooleanFieldHandler()
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
	public String typeName()
	{
		return Boolean.TYPE.getName();
	}

	@Override
	public void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException
	{
		boolean b = field.getBoolean(src);
		field.setBoolean(dst, b);
	}
}
