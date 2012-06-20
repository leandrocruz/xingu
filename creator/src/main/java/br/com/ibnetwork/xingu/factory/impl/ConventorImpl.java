package br.com.ibnetwork.xingu.factory.impl;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Conventor;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.factory.FactoryException;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class ConventorImpl
	implements Conventor
{
	@Inject
	private Factory factory;
	
	@Override
	public Object apply(Class<?> base, String suffix, Object... params)
		throws FactoryException
	{
        String name = base.getName() + suffix;
        Class<?> clazz = null;
        
        try
        {
            clazz = ObjectUtils.loadClass(name);
        }
        catch(Throwable t) 
        {}

        if(clazz == null)
        {
        	return null;
        }
        return factory.create(clazz, params);
	}

}
