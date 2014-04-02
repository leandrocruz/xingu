package br.com.ibnetwork.xingu.utils.inspector.impl;

import br.com.ibnetwork.xingu.utils.inspector.TypeAlias;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class TypeAliasImpl
	implements TypeAlias
{
	private String	name;

	private Type	type;

	public TypeAliasImpl(String name, Type type)
	{
		this.name = name;
		this.type = type;
	}

	public TypeAliasImpl(String name)
	{
		this.name = name;
		this.type = Type.OBJECT;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public Type type()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return name + "[" + type + "]";
	}
}
