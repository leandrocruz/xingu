package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class IntegerFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new IntegerFieldHandler();
	
	private IntegerFieldHandler()
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
		int i = field.getInt(src);
		field.setInt(dst, i);
	}

	@Override
	public String typeName()
	{
		return Integer.TYPE.getName();
	}

}
