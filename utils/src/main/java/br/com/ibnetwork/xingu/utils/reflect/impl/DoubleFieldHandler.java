package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class DoubleFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new DoubleFieldHandler();
	
	private DoubleFieldHandler()
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
		double d = field.getDouble(src);
		field.setDouble(dst, d);
	}

	@Override
	public String typeName()
	{
		return Double.TYPE.getName();
	}
}