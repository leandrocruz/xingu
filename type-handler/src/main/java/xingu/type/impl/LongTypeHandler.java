package xingu.type.impl;

import xingu.type.ObjectType.Type;

public class LongTypeHandler
	extends TypeHandlerSupport
{
	public LongTypeHandler()
	{
		super(Long.class, "long", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Long.valueOf(value);
	}

	@Override
	public String toString(Object obj)
	{
		Long myLong = Long.class.cast(obj);
		return String.valueOf(myLong);
	}
}
