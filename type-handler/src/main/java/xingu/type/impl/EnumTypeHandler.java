package xingu.type.impl;

import xingu.lang.NotImplementedYet;
import xingu.type.ObjectType.Type;

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
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
