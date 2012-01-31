package br.com.ibnetwork.xingu.container;

import org.junit.AfterClass;
import org.junit.Before;

import br.com.ibnetwork.xingu.utils.FSUtils;

public class XinguTestCase 
{
	private static Container container;

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
            Binder binder = container.lookup(Binder.class);
            rebind(binder);
	    }
	    injectDependencies();
	}
    
	protected void rebind(Binder binder)
    {}

    public void injectDependencies()
        throws Exception
	{
	    Injector injector = container.lookup(Injector.class);
	    injector.injectDependencies(this);
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
