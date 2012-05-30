package br.com.ibnetwork.xingu.container;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.reset;

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
    
	protected void rebind(Binder binder)
    {}

    public void injectDependencies()
        throws Exception
	{
	    Injector injector = container.lookup(Injector.class);
	    injector.injectDependencies(this);
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

    
    @AfterClass
    public static void shutdown()
    {
        if(container != null)
        {
            container.stop();
            container = null;
        }
    }
}
