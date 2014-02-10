package br.com.ibnetwork.xingu.factory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;
import br.com.ibnetwork.xingu.factory.Creator;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.factory.FactoryException;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public abstract class FactorySupport
    implements Factory, Configurable
{
    @Inject
    private Container container;

    @Inject
    private ConfigurationManager configurationManager;

    protected Creator creator;
 
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        String defaultCreatorClassName = conf.getAttribute("creator", SimpleCreator.class.getName());
        creator = (Creator) ObjectUtils.getInstance(defaultCreatorClassName);
    }

    @Override
    public Object create(String className) 
        throws FactoryException
    {
    	return create(className, null, null, (Object[]) null);
    }

	@Override
	public Object create(String className, ClassLoader cl)
		throws FactoryException
	{
		return create(className, null, cl, (Object[]) null);
	}

	@Override
	public Object create(String className, Configuration conf, ClassLoader cl)
		throws FactoryException
	{
		return create(className, conf, cl, (Object[]) null);
	}

    @Override
    public Object create(String className, Object... params)
        throws FactoryException
    {
        return create(className, null, null, params);
    }

	@Override
	public Object create(String className, ClassLoader cl, Object... params)
		throws FactoryException
	{
		return create(className, null, cl, params);
	}

    @Override
    public Object create(String className, Configuration conf)
        throws FactoryException
    {
        return create(className, conf, (Object[]) null);
    }

    @Override
    public Object create(String className, Configuration conf, Object... params)
        throws FactoryException
    {
    	return create(className, conf, null, (Object[]) null);
    }

    @Override
	public Object create(String className, Configuration conf, ClassLoader cl, Object... params)
		throws FactoryException
	{
    	Class<?> clazz = ObjectUtils.loadClass(className, cl);
        return create(clazz, conf, params);
	}

    @Override
    public <T> T create(Class<? extends T> clazz) 
        throws FactoryException
    {
        return create(clazz, null, (Object[]) null);
    }

    @Override
    public <T> T create(Class<? extends T> clazz, Configuration conf)
        throws FactoryException
    {
        return create(clazz, conf, (Object[]) null);
    }

    @Override
    public <T> T create(Class<? extends T> clazz, Object... params)
        throws FactoryException
    {
        return create(clazz, null, (Object[]) params);
    }

    protected void addDefaultMappings(Map<String, Class<?>> map)
    {
        map.put(List.class.getName(), ArrayList.class);
        map.put(Set.class.getName(), TreeSet.class);
        map.put(Map.class.getName(), HashMap.class);
    }
    
    protected Class<?> defaultImplOrNull(Class<?> clazz)
    {
        String className = clazz.getName();
        int lastDot = className.lastIndexOf(".");
        String packageName = className.substring(0,lastDot);
        String defaultImpl = packageName + ".impl." + clazz.getSimpleName() + "Impl";
        try
        {
            Class<?> result = ObjectUtils.loadClass(defaultImpl);
            return result;
        }
        catch(Throwable e)
        {
            //ignored
            //log.trace("error creating class: "+defaultImpl,e);
        }
        return null;
    }

    protected <T> T instantiate(Class<T> clazz, Object... params)
    {
        try
        {
            return creator.create(clazz, params);
        }
        catch (Exception e)
        {
            throw new FactoryException("Error creating instance of '"+clazz.getName()+"'", e);
        }
    }

    protected Configuration configurationFor(Class<?> clazz)
    {
        try
        {
            Configuration conf = configurationManager.configurationFor(clazz);
            return conf;
        }
        catch (ConfigurationException e)
        {
            throw new FactoryException("Error searching configuration for '"+clazz.getName()+"'", e);
        }
    }

    protected <T> void startLifecyle(T object, Configuration conf)
    {
        try
        {
            container.startLifecycle(object,conf);
        }
        catch (Exception e)
        {
            throw new FactoryException("Error handling lifecycle of '"+object.getClass().getName()+"'", e);
        }
        
    }
}
