package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.Collections;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class EmptyListTypeHandler
	extends TypeHandlerSupport
{
	public EmptyListTypeHandler()
	{
		super(Collections.EMPTY_LIST.getClass(), "emptyList", Type.COLLECTION);
	}

	@Override
	public Object newInstance()
		throws Exception
	{
		return Collections.emptyList();
	}
}
