package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class ByteTypeHandler
	extends TypeHandlerSupport
{
	public ByteTypeHandler()
	{
		super(Byte.class, "byte", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Byte.valueOf(value);
	}

	@Override
	public String toString(Object obj)
	{
		Byte myByte = Byte.class.cast(obj);
		return String.valueOf(myByte);
	}
}
