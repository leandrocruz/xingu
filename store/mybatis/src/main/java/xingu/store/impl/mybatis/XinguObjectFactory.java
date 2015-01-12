package xingu.store.impl.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.ObjectFactory;

import xingu.factory.Factory;
import xingu.lang.NotImplementedYet;

public class XinguObjectFactory
    implements ObjectFactory
{
    private final Factory factory;

    public XinguObjectFactory(Factory factory)
    {
        this.factory = factory;
    }

	@Override
	public void setProperties(Properties properties)
	{
		throw new NotImplementedYet();
	}

	@Override
	public <T> T create(Class<T> type)
	{
		return factory.create(type);
	}

	@Override
	public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs)
	{
        return factory.create(type, constructorArgs.toArray());

	}

	@Override
	public <T> boolean isCollection(Class<T> type)
	{
		return Collection.class.isAssignableFrom(type);
	}
}
