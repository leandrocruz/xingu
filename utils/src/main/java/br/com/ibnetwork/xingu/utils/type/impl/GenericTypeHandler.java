package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class GenericTypeHandler
	extends TypeHandlerSupport
{
	public GenericTypeHandler(Class<?> clazz, String name, Type type)
	{
		super(clazz, name, type);
	}
}
