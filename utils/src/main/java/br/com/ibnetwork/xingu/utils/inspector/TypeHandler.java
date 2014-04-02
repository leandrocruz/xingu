package br.com.ibnetwork.xingu.utils.inspector;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public interface TypeHandler
{
	Type type();

	String name();
	
	Class<?> clazz();

	Object newInstanceWith(String value);
}