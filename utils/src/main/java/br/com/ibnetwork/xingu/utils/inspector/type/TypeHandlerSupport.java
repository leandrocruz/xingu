package br.com.ibnetwork.xingu.utils.inspector.type;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.inspector.TypeHandler;

public class TypeHandlerSupport
	implements TypeHandler
{
	private String		name;

	private Type		type;

	private Class<?>	clazz;

	public TypeHandlerSupport(Class<?> clazz, String name, Type type)
	{
		this.clazz = clazz;
		this.name  = name;
		this.type  = type;
	}

	public TypeHandlerSupport(String name)
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
	public Class<?> clazz()
	{
		return clazz;
	}

	@Override
	public String toString()
	{
		return name + "[" + type + "]";
	}

	@Override
	public Object newInstanceWith(String value)
	{
		throw new NotImplementedYet();
	}

}
