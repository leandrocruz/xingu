package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.Collections;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class EmptySetTypeHandler
	extends TypeHandlerSupport
{
	public EmptySetTypeHandler()
	{
		super(Collections.EMPTY_SET.getClass(), "emptySet", Type.COLLECTION);
	}

	@Override
	public Object newInstance()
		throws Exception
	{
		return Collections.emptySet();
	}
}
