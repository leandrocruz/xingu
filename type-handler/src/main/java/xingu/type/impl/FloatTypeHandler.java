package xingu.type.impl;

import xingu.type.ObjectType.Type;

public class FloatTypeHandler
	extends TypeHandlerSupport
{
	public FloatTypeHandler()
	{
		super(Float.class, "float", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Float.valueOf(value);
	}

	@Override
	public String toString(Object obj)
	{
		Float myFloat = Float.class.cast(obj);
		return String.valueOf(myFloat);
	}
}
