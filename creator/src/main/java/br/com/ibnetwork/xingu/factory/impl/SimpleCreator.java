package br.com.ibnetwork.xingu.factory.impl;

import java.lang.reflect.Constructor;

import br.com.ibnetwork.xingu.factory.Creator;
import br.com.ibnetwork.xingu.lang.Constructors;

public class SimpleCreator
    implements Creator
{
    public <T> T create(Class<? extends T> clazz, Object... params) 
    	throws Exception
    {
        Constructor<? extends T> constructor = Constructors.findConstructor(clazz, params);
        constructor.setAccessible(true);
        T result = constructor.newInstance(params);
        return result;
    }
}
