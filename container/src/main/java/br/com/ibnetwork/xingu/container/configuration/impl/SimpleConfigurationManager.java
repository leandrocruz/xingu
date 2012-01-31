package br.com.ibnetwork.xingu.container.configuration.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;

public class SimpleConfigurationManager
    implements ConfigurationManager, Configurable, Initializable
{
    private Map<String, Configuration> registry = new HashMap<String, Configuration>();
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {}

    public void initialize() 
    	throws Exception
    {}

    @Override
    public Configuration configurationFor(String roleName)
        throws ConfigurationException
    {
        return configurationFor(roleName, null);   
    }

    @Override
    public Configuration configurationFor(Class<?> role)
        throws ConfigurationException
    {
        return configurationFor(role.getName());
    }

    @Override
    public Configuration configurationFor(Class<?> role, String key)
        throws ConfigurationException
    {
        return configurationFor(role.getName(), key);
    }

    @Override
    public Configuration configurationFor(String roleName, String key)
        throws ConfigurationException
    {
        if(!StringUtils.isEmpty(key))
        {
            roleName +=":"+key;
        }
        return registry.get(roleName);
    }

    @Override
    public void register(String roleName, String key, Configuration conf)
    {
        if(!StringUtils.isEmpty(key))
        {
            roleName +=":"+key;
        }
        registry.put(roleName, conf);
    }
}
