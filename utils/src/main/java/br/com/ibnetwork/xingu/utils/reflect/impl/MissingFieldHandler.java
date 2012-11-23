package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class MissingFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new MissingFieldHandler();
	
	private MissingFieldHandler()
	{}

	public static FieldHandler instance()
	{
		return instance;
	}

	@Override
	public boolean isPrimitive()
	{
		throw new NotImplementedYet();
	}

	@Override
	public void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException
	{
		throw new NotImplementedYet();
	}

	@Override
	public String typeName()
	{
		throw new NotImplementedYet();
	}
}
