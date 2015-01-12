package xingu.container.configuration;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

public interface ConfigurationManager
{
    Configuration configurationFor(String roleName)
    	throws ConfigurationException;

    Configuration configurationFor(String roleName, String key)
        throws ConfigurationException;

    Configuration configurationFor(Class<?> role)
		throws ConfigurationException;

    Configuration configurationFor(Class<?> role, String key)
        throws ConfigurationException;

    void register(String roleName, String key, Configuration conf);
}
