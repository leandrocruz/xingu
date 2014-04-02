package br.com.ibnetwork.xingu.utils.inspector;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public interface TypeAliasMap
{
	TypeAlias aliasFor(Class<?> clazz, Type type);
}
