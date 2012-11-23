package br.com.ibnetwork.xingu.utils.reflect.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.reflect.FieldHandler;

public class CharacterFieldHandler
	implements FieldHandler
{
	private static final FieldHandler instance = new CharacterFieldHandler();
	
	private CharacterFieldHandler()
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
		return Character.TYPE.getName();
	}

	@Override
	public void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException
	{
		char c = field.getChar(src);
		field.setChar(dst, c);
	}
}
