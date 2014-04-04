package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class DoubleTypeHandler
	extends TypeHandlerSupport
{
	public DoubleTypeHandler()
	{
		super(Double.class, "double", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Double.valueOf(value);
	}

	@Override
	public String toString(Object obj)
	{
		Double myDouble = Double.class.cast(obj);
		return String.valueOf(myDouble);
	}
}
