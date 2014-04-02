package br.com.ibnetwork.xingu.utils.inspector.type;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class StringTypeHandler
	extends TypeHandlerSupport
{
	public StringTypeHandler()
	{
		super(String.class, "string", Type.PRIMITIVE);
	}

	@Override
	public Object newInstanceWith(String value)
	{
		return value;
	}
}