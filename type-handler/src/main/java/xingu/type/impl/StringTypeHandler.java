package xingu.type.impl;

import xingu.type.ObjectType.Type;

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