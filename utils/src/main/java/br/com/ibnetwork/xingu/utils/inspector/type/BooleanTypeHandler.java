package br.com.ibnetwork.xingu.utils.inspector.type;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class BooleanTypeHandler
	extends TypeHandlerSupport
{
	public BooleanTypeHandler()
	{
		super(Boolean.class, "bool", Type.PRIMITIVE);
	}

	@Override
	public Object toObject(String value)
	{
		return Boolean.valueOf(value);
	}
}