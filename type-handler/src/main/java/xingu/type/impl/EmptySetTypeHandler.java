package xingu.type.impl;

import java.util.Collections;

import xingu.type.ObjectType.Type;

public class EmptySetTypeHandler
	extends TypeHandlerSupport
{
	public EmptySetTypeHandler()
	{
		super(Collections.EMPTY_SET.getClass(), "emptySet", Type.COLLECTION);
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		return Collections.emptySet();
	}
}
