package br.com.ibnetwork.xingu.utils.type;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public interface TypeHandlerRegistry
{
	void register(TypeHandler handler);
	
	TypeHandler handlerFor(Class<?> clazz, Type type);

	TypeHandler get(String nameOrClass);
}
