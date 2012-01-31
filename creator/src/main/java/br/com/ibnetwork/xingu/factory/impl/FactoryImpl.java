package br.com.ibnetwork.xingu.factory.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.factory.FactoryException;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class FactoryImpl
    extends FactorySupport
{
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private Map<String, Class<?>> map = new HashMap<String, Class<?>>();

    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        super.configure(conf);
        addDefaultMappings(map);
        Configuration[] classes = conf.getChild("classes").getChildren("class");
        for (Configuration configuration : classes)
        {
            String name = configuration.getAttribute("name");
            String concrete = configuration.getAttribute("concrete");
            map.put(name, ObjectUtils.loadClass(concrete));
            log.debug("Class '{}' mapped to '{}'", name, concrete);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<T> mappingFor(Class<T> clazz)
    {
        return (Class<T>) map.get(clazz.getName());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<? extends T> clazz, Configuration conf, Object... params) 
        throws FactoryException
    {
        Class<? extends T> theClazz = mappingFor(clazz);
        if(theClazz == null && clazz.isInterface()) 
        {
            theClazz = (Class<? extends T>) defaultImplOrNull(clazz);
            if(theClazz == null)
            {
                throw new FactoryException("Can't find a suitable implementation for '"+clazz.getName()+"'");
            }
        }
        if(theClazz == null)
        {
            theClazz = clazz;
        }
        
        T result = instantiate(theClazz, params);
        
        if(conf == null)
        {
            conf = configurationFor(theClazz);
        }
        startLifecyle(result, conf);
        return result;
    }

    
}
