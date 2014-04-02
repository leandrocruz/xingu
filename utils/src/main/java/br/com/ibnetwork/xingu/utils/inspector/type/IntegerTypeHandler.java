package br.com.ibnetwork.xingu.utils.inspector.type;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class IntegerTypeHandler
	extends TypeHandlerSupport
{
	public IntegerTypeHandler()
	{
		super(Integer.class, "int", Type.PRIMITIVE);
	}

	@Override
	public Object newInstanceWith(String value)
	{
		return Integer.valueOf(value);
	}
}