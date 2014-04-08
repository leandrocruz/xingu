package br.com.ibnetwork.xingu.container.impl;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Binding;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerException;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;
import br.com.ibnetwork.xingu.utils.ObjectUtils;
import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public abstract class ContainerSupport
    implements Container
{
    protected Logger log = LoggerFactory.getLogger(getClass());
    
    public void startLifecycle(Object component, Configuration conf)
        throws Exception
    {
        if (conf == null)
        {
            conf = new DefaultConfiguration("config");
        }
        
        injector().injectDependencies(component);
        if (component instanceof Configurable && conf != null)
        {
            ((Configurable) component).configure(conf);
        }
        if (component instanceof Initializable)
        {
            ((Initializable) component).initialize();
        }
        if (component instanceof Startable)
        {
            ((Startable) component).start();
        }
    }

    public void stopLifecycle(Object obj) 
        throws Exception
    {
        if (obj instanceof Startable)
        {
            ((Startable) obj).stop();
        }
        if (obj instanceof Disposable)
        {
            ((Disposable) obj).dispose();
        }
    }

    protected Injector injector()
    {
        return binder().get(Injector.class).impl();
    }

    protected ConfigurationManager configurationManager()
    {
        return binder().get(ConfigurationManager.class).impl();
    }
    
    @SuppressWarnings("unchecked")
	protected <T> Binding<T> binding(SimpleClassLoader cl, String roleName, String key, String implName) 
        throws ConfigurationException
    {
        Class<T> clazz = null;
        T impl = null;
        try
        {
            clazz = (Class<T>) ObjectUtils.loadClass(roleName, cl.getClassLoader());
            impl = (T) ObjectUtils.getInstance(implName, cl.getClassLoader());    
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error binding '"+roleName+"' to '"+implName+"'",e);
        }
        Binding<T> binding = binder().bind(clazz, key);
        binding.to(impl);
        return binding;
    }

    protected <T> T tryDefaults(Class<T> clazz, SimpleClassLoader cl) 
        throws ContainerException
    {
        String role = clazz.getName(); 
        String packageName = role.substring(0,role.lastIndexOf("."));
        String componentName = role.substring(role.lastIndexOf(".")+1,role.length());
        String className = packageName + ".impl."+componentName+"Impl";
        T component = (T) ObjectUtils.getInstance(className, cl.getClassLoader());
        return component;
    }
}
