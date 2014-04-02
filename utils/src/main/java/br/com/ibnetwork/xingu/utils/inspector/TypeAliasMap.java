package br.com.ibnetwork.xingu.utils.inspector;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public interface TypeAliasMap
{
	void put(Class<?> clazz, TypeAlias alias);
	
	TypeAlias aliasFor(Class<?> clazz, Type type);
}
