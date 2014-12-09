package xingu.type.impl;

import xingu.type.ObjectType.Type;

public class BooleanTypeHandler
	extends TypeHandlerSupport
{
	public BooleanTypeHandler()
	{
		super(Boolean.class, "boolean", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		switch(value)
		{
			case "1":
			case "true":
			case "y":
			case "s":
			case "sim":
				return Boolean.TRUE;

			default:
				return Boolean.FALSE;
		}
	}

	@Override
	public String toString(Object obj)
	{
		Boolean myBoolean = Boolean.class.cast(obj);
		return String.valueOf(myBoolean);
	}
}