package xingu.type;

import xingu.type.ObjectType.Type;

public interface TypeHandlerRegistry
{
	void register(TypeHandler handler);
	
	TypeHandler handlerFor(Class<?> clazz, Type type);

	TypeHandler get(String nameOrClass);
}
