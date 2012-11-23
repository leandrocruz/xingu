package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class ShortFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new ShortFieldHandler();
	
	private ShortFieldHandler()
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
		short s = field.getShort(src);
		field.setShort(dst, s);
	}

	@Override
	public String typeName()
	{
		return Short.TYPE.getName();
	}

}
