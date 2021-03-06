package xingu.type.impl;

import java.util.Collections;

import xingu.type.ObjectType.Type;

public class EmptyListTypeHandler
	extends TypeHandlerSupport
{
	public EmptyListTypeHandler()
	{
		super(Collections.EMPTY_LIST.getClass(), "emptyList", Type.COLLECTION);
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		return Collections.emptyList();
	}
}
