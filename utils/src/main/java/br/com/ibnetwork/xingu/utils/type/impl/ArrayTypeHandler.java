package br.com.ibnetwork.xingu.utils.type.impl;

import java.lang.reflect.Array;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class ArrayTypeHandler
	extends TypeHandlerSupport
{
	public ArrayTypeHandler(Class<?> clazz, String name)
	{
		super(clazz, name, Type.ARRAY);
	}

	@Override
	public Object newInstance()
		throws Exception
	{
		Class<?> target = clazz().getComponentType();
		return Array.newInstance(target, 0);
	}
}
