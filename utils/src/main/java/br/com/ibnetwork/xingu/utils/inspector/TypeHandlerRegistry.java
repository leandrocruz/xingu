package br.com.ibnetwork.xingu.utils.inspector;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public interface TypeHandlerRegistry
{
	void register(TypeHandler handler);
	
	TypeHandler handlerFor(Class<?> clazz, Type type);

	TypeHandler get(String nameOrClass);
}
