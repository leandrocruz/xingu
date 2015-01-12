package xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import xingu.utils.reflect.FieldHandler;

public class LongFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new LongFieldHandler();
	
	private LongFieldHandler()
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
		long l = field.getLong(src);
		field.setLong(dst, l);
	}

	@Override
	public String typeName()
	{
		return Long.TYPE.getName();
	}

}
