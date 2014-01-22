package br.com.ibnetwork.xingu.ibatis.factory;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerUtils;
import br.com.ibnetwork.xingu.factory.Factory;

import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;

public class XinguObjectFactory
    implements ResultObjectFactory
{
    private static Factory factory;
    
    static 
    {
        try
        {
            Container container = ContainerUtils.getContainer();
            factory = (Factory) container.lookup(Factory.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Object createInstance(String statementId, Class clazz)
            throws InstantiationException, IllegalAccessException
    {
        Object obj = factory.create(clazz); 
        return obj;
    }

    public void setProperty(String name, String value)
    {
        System.out.println(getClass().getName() + " setting property "+name+" = "+value);
    }

}
