package xingu.type.impl;

import xingu.type.ObjectType.Type;


public class GenericTypeHandler
	extends TypeHandlerSupport
{
	public GenericTypeHandler(Class<?> clazz, String name, Type type)
	{
		super(clazz, name, type);
	}
}
