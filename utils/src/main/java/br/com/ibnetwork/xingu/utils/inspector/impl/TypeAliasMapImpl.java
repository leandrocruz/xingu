package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.inspector.TypeAlias;
import br.com.ibnetwork.xingu.utils.inspector.TypeAliasMap;

public class TypeAliasMapImpl
	implements TypeAliasMap
{
	private static Map<Class<?>, TypeAlias> aliasByClass = new HashMap<Class<?>, TypeAlias>();
	
	static {
		aliasByClass.put(String.class, new TypeAliasImpl("string"));
	}
	
	@Override
	public TypeAlias aliasFor(Class<?> clazz, Type type)
	{
		TypeAlias alias = aliasByClass.get(clazz);
		if(alias == null)
		{
			alias = getAlias(clazz, type);
			aliasByClass.put(clazz, alias);
		}
		return alias;
	}

	private TypeAlias getAlias(Class<?> clazz, Type type)
	{
		String name = clazz.getName();
		switch(type)
		{
			case ARRAY:
				Class<?> componentType = clazz.getComponentType();
				name = aliasFor(componentType, null).name();
				break;

			default:
				break;
		}
		return new TypeAliasImpl(name, type);
	}
}
