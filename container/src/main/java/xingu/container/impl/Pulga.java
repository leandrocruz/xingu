package xingu.container.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;

import xingu.container.Binder;
import xingu.container.Binding;
import xingu.container.ConfigurationLoader;
import xingu.container.Container;
import xingu.container.ContainerException;
import xingu.container.FieldInjector;
import xingu.container.Injector;
import xingu.container.configuration.ConfigurationManager;
import xingu.container.configuration.impl.SimpleConfigurationManager;
import xingu.utils.ObjectUtils;
import xingu.utils.classloader.NamedClassLoader;
import xingu.utils.classloader.impl.ClassLoaderAdapter;

public class Pulga
    extends ContainerSupport
	implements Container
{
	private Container			parent;

	private List<Class<?>>		earlyInit	= new ArrayList<Class<?>>();

	private Stack<Object>		stack;

	private Binder				binder;

	private NamedClassLoader	cl;

	private Configuration		conf;

	private boolean				configured;

	public Pulga(Container parent, InputStream is)
		throws Exception
	{
		this(parent, is, new ClassLoaderAdapter("context", Thread.currentThread().getContextClassLoader()));
	}

	public Pulga(Container parent, InputStream is, NamedClassLoader cl)
		throws Exception
	{
    	stack     = new Stack<Object>();
        binder    = new SimpleBinder();
        binder.bind(Injector.class).to(new FieldInjector(this));

        this.parent = parent;
        this.cl   = cl;
        this.conf = ConfigurationLoader.load(is);
        IOUtils.closeQuietly(is);
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
            Configuration conf     = array[i];
            String        roleName = conf.getAttribute("role");
            String        key      = conf.getAttribute("key", null);
            configurationManager.register(roleName, key, conf);
            
            String     implName = conf.getAttribute("class");
            Binding<?> binding  = binding(cl, roleName, key, implName);
            
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
            if(parent != null)
            {
            	Binding<T> onParent = parent.binder().get(role, key);
            	if(onParent != null && !onParent.isDefault())
            	{
            		return parent.lookup(role, key);
            	}
            }

        	impl = tryDefaults(role, cl);
            if (impl == null)
            {
                // give up
                throw new ContainerException("No component found for key["+ key + "] role[" + role + "]");
            }
            binding = binder.bind(role);
            binding.to(impl).asDefault();
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
            	Configuration conf = binding.configuration();
            	if(conf == null)
            	{
            		conf = configurationManager().configurationFor(role, key);
            	}
                startLifecycle(impl, conf);
            }
            catch (Exception e)
            {
                String msg = "Error initializing component: " + role.getName() + "[" + key + "]";
                //log.info(msg);
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
        return "Pulga@"+System.identityHashCode(this);
    }
}