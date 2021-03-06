package xingu.type.impl;

import java.util.Collections;

import xingu.type.ObjectType.Type;

public class EmptyMapTypeHandler
	extends TypeHandlerSupport
{
	public EmptyMapTypeHandler()
	{
		super(Collections.EMPTY_MAP.getClass(), "emptyMap", Type.COLLECTION);
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		return Collections.emptyMap();
	}
}
