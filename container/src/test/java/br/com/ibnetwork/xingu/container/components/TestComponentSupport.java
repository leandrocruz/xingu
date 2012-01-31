package br.com.ibnetwork.xingu.container.components;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public abstract class TestComponentSupport
	implements TestComponent, Serviceable,Configurable,Initializable,Startable
{
    private boolean composed;
    
    private boolean configured;
    
    private boolean initialized;
    
    private boolean started;
    
    Configuration conf;
    
    public void service(ServiceManager manager)
    	throws ServiceException
    {
        this.composed = true;
    }

    public void configure(Configuration conf)
    	throws ConfigurationException
    {
        this.configured = true;
        this.conf = conf;
    }

    public void initialize()
    	throws Exception
    {
        this.initialized = true;
    }

    public void start()
    	throws Exception
    {
        this.started = true;
    }
    
    public void stop()
    	throws Exception
    {}

    public boolean isComposed()
    {
        return composed;
    }

    public boolean isConfigured()
    {
        return configured;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public boolean isStarted()
    {
        return started;
    }

    public Configuration getConfiguration()
    {
        return conf;
    }

}
