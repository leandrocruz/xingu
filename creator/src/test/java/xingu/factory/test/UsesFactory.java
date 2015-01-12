package xingu.factory.test;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.lang.NotImplementedYet;

public class UsesFactory
	implements Configurable, Initializable, Startable
{
    private boolean configured; 
    
    private Configuration conf;

    private boolean initialized;

    private boolean started;

    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        configured = true;
        this.conf = conf;
    }

    public boolean isConfigured()
    {
        return configured;
    }
    
    public Configuration getConfiguration()
    {
        return conf;
    }

    public boolean initialized() 
        throws Exception
    {
        return initialized;
    }
    
    public boolean started() 
        throws Exception
    {
        return started;
    }

    @Override
    public void stop() 
        throws Exception
    {
        throw new NotImplementedYet();
    }

    @Override
    public void initialize() 
        throws Exception
    {
        this.initialized = true;
    }

    @Override
    public void start() 
        throws Exception
    {
        this.started = true;
    }
}
