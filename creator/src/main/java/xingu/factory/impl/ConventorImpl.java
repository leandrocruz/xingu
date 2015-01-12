package xingu.factory.impl;

import xingu.container.Inject;
import xingu.factory.Conventor;
import xingu.factory.Factory;
import xingu.factory.FactoryException;
import xingu.utils.ObjectUtils;

public class ConventorImpl
	implements Conventor
{
	@Inject
	private Factory factory;
	
	@Override
	public Object apply(Class<?> base, String suffix)
		throws FactoryException
	{
		return apply(base, suffix, (Object[]) null);
	}

	@Override
	public Object apply(Class<?> base, String suffix, Object... params)
		throws FactoryException
	{
        String name = base.getName() + suffix;
        Class<?> clazz = null;
        ClassLoader cl = base.getClassLoader();
        
        try
        {
            clazz = ObjectUtils.loadClass(name, cl);
        }
        catch(Throwable t) 
        {}

        if(clazz == null)
        {
        	return null;
        }
        
        return ObjectUtils.getInstance(clazz);
	}

}
