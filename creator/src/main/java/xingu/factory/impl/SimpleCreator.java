package xingu.factory.impl;

import java.lang.reflect.Constructor;

import xingu.factory.Creator;
import xingu.lang.Constructors;

public class SimpleCreator
    implements Creator
{
	@Override
	public <T> T create(Class<? extends T> clazz, Object... params)
		throws Exception
	{
        Constructor<? extends T> constructor = Constructors.findConstructor(clazz, params);
        constructor.setAccessible(true);
        T result = constructor.newInstance(params);
        return result;
	}
}
