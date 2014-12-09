package xingu.type.impl;

import xingu.type.ObjectType.Type;

public class CharTypeHandler
	extends TypeHandlerSupport
{
	public CharTypeHandler()
	{
		super(Character.class, "char", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		Character input = value.charAt(0);
		return Character.valueOf(input);
	}
	
	@Override
	public String toString(Object obj)
	{
		Character myChar = Character.class.cast(obj);
		return String.valueOf(myChar);
	}
}