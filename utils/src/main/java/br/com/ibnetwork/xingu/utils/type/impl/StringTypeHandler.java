package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class StringTypeHandler
	extends TypeHandlerSupport
{
	public StringTypeHandler()
	{
		super(String.class, "string", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return value;
	}
}