package xingu.container;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.internal.util.MockUtil;

public class XinguTestCase 
{
	private static Container	container;

	private static MockUtil		mockUtil	= new MockUtil();

	private static Binder		binder;

    protected Container getContainer()
    	throws Exception
    {
    	if(container == null)
    	{
    		String      fileName = getContainerFile();
    		File        file     = new File(fileName);
    		InputStream is       = null;
    		if(file.exists())
    		{
    			is = new FileInputStream(file);
    		}
    		else
    		{
    			URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
    			is           = resource.openStream();
    		}
    		container = ContainerUtils.createContainer(is);
    		container.configure();
    		container.start();
    	}
    	return container;
    }
    
	protected String getContainerFile()
    {
	    return "pulga-empty.xml";
    }

	protected boolean resetContainer()
    {
        return false;
    }

	@Before
	public void prepare()
	    throws Exception
	{
	    if(resetContainer() && container != null)
        {
            container.stop();
            container = null;
        }
	    if(container == null)
	    {
            container = getContainer();
            binder = container.lookup(Binder.class);
            rebind(binder);
	    }
	    injectDependencies();
	    resetAllMocks();
	}
	
    @AfterClass
    public static void shutdown()
    {
        if(container != null)
        {
            container.stop();
            container = null;
        }
    }
    
	protected void rebind(Binder binder)
		throws Exception
    {}

    public void injectDependencies()
        throws Exception
	{
	    Injector injector = container.lookup(Injector.class);
	    injector.injectDependencies(this);
	}

    protected <T> void withMock(String key, Class<T> clazz)
	{
    	T impl = mock(clazz);
    	binder.bind(clazz, key).to(impl);
	}

    protected <T> void withMock(Class<T> clazz)
	{
    	T impl = mock(clazz);
    	binder.bind(clazz).to(impl);
	}

    protected void resetAllMocks()
	{
    	List<Class<?>> roles = binder.getRoles();
    	for (Class<?> role : roles)
		{
    		Binding<?> binding = binder.get(role);
    		if(binding == null)
    		{
    			continue;
    		}
    		
    		Object impl = binding.impl();
    		if(mockUtil.isMock(impl))
    		{
    			reset(impl);
    		}
		}
	}

    protected File getFile(String name)
    {
    	URL url = getResource(name);
    	return new File(url.getFile());
    }
    
	protected URL getResource(String name)
	{
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}

	protected Configuration buildFrom(String xml)
	{
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
		InputStream is = null;
		
		try
		{
			is = IOUtils.toInputStream(xml);
			return builder.build(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
		return null;
	}
}
