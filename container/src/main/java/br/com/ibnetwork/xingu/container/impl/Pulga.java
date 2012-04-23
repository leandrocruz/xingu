package br.com.ibnetwork.xingu.container.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;

import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Binding;
import br.com.ibnetwork.xingu.container.ConfigurationLoader;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.ContainerException;
import br.com.ibnetwork.xingu.container.FieldInjector;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;
import br.com.ibnetwork.xingu.container.configuration.impl.SimpleConfigurationManager;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class Pulga
    extends ContainerSupport
	implements Container
{
    /* holds keys for components that require early init */
    private List<Class<?>> earlyInit = new ArrayList<Class<?>>();
    
    private Stack<Object> stack;
    
    private Binder binder;
    
    private String file;
    
    private Configuration conf;

	private boolean configured;
    
    public Pulga(String fileName)
        throws Exception
    {
        stack = new Stack<Object>();
        binder = new SimpleBinder();
        binder.bind(Injector.class).to(new FieldInjector(this));
        this.file = fileName;
        
        if(fileName == null)
        {
            log.info("Creating container without configuration");
            conf = new DefaultConfiguration("pulga");
        }
        else
        {
            log.info("Creating container from file["+fileName+"]");
            conf = ConfigurationLoader.load(fileName);  
        }
    }

	@Override
	public void configure()
		throws ContainerException
	{
		log.info("Configuring Pulga '"+this+"'");
        try
        {
            configure(conf);
            configured = true;
        }
        catch (ConfigurationException e)
        {
            throw new ContainerException("Configuration error", e);
        }
        log.info("Pulga '"+this+"' configured");
	}

	public void start()
		throws ContainerException
	{
		log.info("Starting Pulga '"+this+"'");
		if(!configured)
		{
			throw new ContainerException("Pulga '"+this+"' not configured");
		}
        for (Class<?> role : earlyInit)
        {
            lookup(role);
            log.info("Component '{}' early initialization completed", role.getName());
        }
        log.info("Pulga '"+this+"' started");
        
	}

    public void stop()
    	throws ContainerException
    {
        log.info("Stopping Pulga '"+this+"'");
        while(!stack.empty())
        {
            Object component = stack.pop();
            log.info("Stopping component '{}'", component.getClass().getName());
            try
            {
                stopLifecycle(component);
            }
            catch(Throwable t)
            {
                log.error("Error stopping component "+component.getClass().getName(), t);
            }
        }
        log.info("Pulga '"+this+"' stopped");
    }

    public void configure(Configuration pulgaConf)
		throws ConfigurationException
	{
        ConfigurationManager configurationManager = buildConfigurationManager(pulgaConf);
        Configuration[] array = pulgaConf.getChildren("component");
        for (int i = 0; i < array.length; i++)
        {
            Configuration conf = array[i];
            String roleName = conf.getAttribute("role");
            String key = conf.getAttribute("key", null);
            configurationManager.register(roleName, key, conf);
            
            String implName = conf.getAttribute("class");
            Binding<?> binding = binding(roleName, key, implName);
            
            boolean initialize = conf.getAttributeAsBoolean("initialize", false);
            initialize = initialize || conf.getAttributeAsBoolean("earlyInit", false);
            if(initialize)
            {
                earlyInit.add(binding.role());
            }
            log.info("Component role["+roleName+"] impl["+implName+"] registered");
        }
	}

    private ConfigurationManager buildConfigurationManager(Configuration pulgaConf) 
        throws ConfigurationException
    {
        SimpleConfigurationManager configurationManager = new SimpleConfigurationManager();
        configurationManager.configure(pulgaConf);
        try
        {
            configurationManager.initialize();
        }
        catch (Exception e)
        {
            throw new ConfigurationException("Error initializing ConfigurationManager", e);
        }
        binder.bind(ConfigurationManager.class).to(configurationManager);
        return configurationManager;
    }

    @Override
    public Binder binder()
    {
        return binder;
    }

    public <T> T lookup(Class<T> clazz)
        throws ContainerException
    {
        return lookup(clazz, null);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T lookup(Class<T> role, String key)
        throws ContainerException
    {
        if (Container.class.equals(role))
        {
            return (T) this;
        }
        if(Binder.class.equals(role))
        {
            return (T) binder;
        }
        
        T impl = null;
        Binding<T> binding = binder.get(role, key);
        if (binding == null)
        {
            impl = tryDefaults(role);
            if (impl == null)
            {
                // give up
                throw new ContainerException("No component found for key["+ key + "] role[" + role + "]");
            }
            binding = binder.bind(role);
            binding.to(impl);
        }
        else
        {
            impl = binding.impl();
            if(impl == null)
            {
                Class<T> implClass = binding.implClass();
                impl = (T) ObjectUtils.getInstance(implClass);
                binding.to(impl);
            }
        }
        if(!binding.isReady())
        {
            try
            {
                Configuration conf = configurationManager().configurationFor(role, key);
                startLifecycle(impl, conf);
            }
            catch (Exception e)
            {
                String msg = "Error initializing component: " + role + "[" + key + "]";
                log.info(msg, e);
                throw new ContainerException(msg, e);
            }
            log.debug("Binding ["+role.getSimpleName()+":"+key+"] to ["+impl+"] is ready");
            binding.isReady(true);
            stack.push(impl);
        }
        return impl;
    }
    
	@Override
	public boolean isResolved(Class<?> clazz, String key)
	{
		Binding<?> binding = binder.get(clazz, key);
		return binding == null ? false : binding.isReady();
	}

    @Override
    public String toString()
    {
        return "Pulga@"+System.identityHashCode(this) + " ("+file+")";
    }
}