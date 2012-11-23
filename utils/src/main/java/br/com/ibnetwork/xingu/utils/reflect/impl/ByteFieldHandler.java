package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class ByteFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new ByteFieldHandler();
	
	private ByteFieldHandler()
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
		return Byte.TYPE.getName();
	}

	@Override
	public void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException
	{
		byte b = field.getByte(src);
		field.setByte(dst, b);
	}
}
