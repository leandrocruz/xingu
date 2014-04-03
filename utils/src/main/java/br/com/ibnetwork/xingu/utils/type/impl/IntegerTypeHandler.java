package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class IntegerTypeHandler
	extends TypeHandlerSupport
{
	public IntegerTypeHandler()
	{
		super(Integer.class, "int", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Integer.valueOf(value);
	}

	@Override
	public String toString(Object obj)
	{
		Integer myInteger = Integer.class.cast(obj);
		return String.valueOf(myInteger);
	}
}
