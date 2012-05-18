package xingu.store.impl.mybatis;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.ObjectFactory;

import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class XinguObjectFactory
    implements ObjectFactory
{
    private final Factory factory;

    public XinguObjectFactory(Factory factory)
    {
        this.factory = factory;
    }

    @Override
    public Object create(Class clazz)
    {
        return factory.create(clazz);
    }

    @Override
    public Object create(Class clazz, List<Class> arg1, List<Object> arg2)
    {
        Object object = factory.create(clazz, arg2.toArray());
        return object;
    }

    @Override
    public void setProperties(Properties props)
    {
        throw new NotImplementedYet();
    }
}
