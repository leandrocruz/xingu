package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class EnumTypeHandler
	extends TypeHandlerSupport
{
	public EnumTypeHandler()
	{
		super(Enum.class, Type.ENUM.name().toLowerCase(), Type.ENUM);
	}
	
	@Override
	public String toString(Object obj)
	{
		return obj.toString();
	}

	@Override
	public Object toObject(String value)
	{
		throw new NotImplementedYet();
	}


	@Override
	public Object newInstance()
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
