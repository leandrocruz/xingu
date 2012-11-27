package br.com.ibnetwork.xingu.container;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import java.io.InputStream;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.internal.util.MockUtil;

import br.com.ibnetwork.xingu.utils.FSUtils;

public class XinguTestCase 
{
	private static Container container;
	
	private static MockUtil mockUtil = new MockUtil();
	
	private static Binder binder;

    protected Container getContainer()
    	throws Exception
    {
    	if(container == null)
    	{
    		String fileName = getContainerFile();
    		fileName = FSUtils.load(fileName);
    		container = ContainerUtils.createContainer(fileName);
    		container.configure();
    		container.start();
    	}
    	return container;
    }
    
	protected String getContainerFile()
    {
	    return "pulga.xml";
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
