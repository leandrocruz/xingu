package br.com.ibnetwork.xingu.utils.inspector.type;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class SimpleTypeHandler
	extends TypeHandlerSupport
{
	public SimpleTypeHandler(Class<?> clazz, String name, Type type)
	{
		super(clazz, name, type);
	}
}
