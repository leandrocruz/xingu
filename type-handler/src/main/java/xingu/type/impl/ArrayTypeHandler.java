package xingu.type.impl;

import java.lang.reflect.Array;

import xingu.type.ObjectType.Type;

public class ArrayTypeHandler
	extends TypeHandlerSupport
{
	public ArrayTypeHandler(Class<?> clazz, String name)
	{
		super(clazz, name, Type.ARRAY);
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		Class<?> target = clazz().getComponentType();
		return Array.newInstance(target, 0);
	}
}
