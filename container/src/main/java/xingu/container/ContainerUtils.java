package xingu.container;

import java.io.InputStream;

import xingu.container.impl.Pulga;
import xingu.utils.FSUtils;
import xingu.utils.io.FileUtils;

public class ContainerUtils
{
	static final String XINGU_CONTAINER_FILE = "XINGU_CONTAINER_FILE";

	private static Container container;
    
    private static final ThreadLocal<Container> local = new ThreadLocal<Container>();
    
    public static void reset()
    {
        if(container == null)
        {
            return;
        }
        container.stop();
        container = null;
    }

    public static final Container getLocalContainer()
    {
    	return local.get();
    }

    public static Container getContainer()
    	throws Exception
    {
    	return getContainer(true);
    }

    public static Container getContainer(boolean start)
        throws Exception
    {
//        if(container != null)
//        {
//            return container;
//        }
        String      name = getFileName();
        InputStream is   = FileUtils.toInputStream(name);
        return getContainer(is, start);
    }

    public static Container getContainer(InputStream is)
    	throws Exception
    {
    	return getContainer(is, true);
    }

    public static Container getContainer(InputStream is, boolean start)
    	throws Exception
    {
        if(container != null)
        {
            return container;
        }
        container = createContainer(is);
        local.set(container);        
        container.configure();
        if(start)
        {
        	container.start();
        }
        return container;
    }

    public static Container createContainer(InputStream is)
    	throws Exception
    {
    	return new Pulga(null, is);
    }

    public static String getFileName()
    {
        String fileName = System.getenv(XINGU_CONTAINER_FILE);
        if(fileName != null)
        {
            return fileName;
        }

        fileName = System.getProperty(XINGU_CONTAINER_FILE);
        if(fileName != null)
        {
            return fileName;
        }
        
        fileName = FSUtils.loadFromClasspath("pulga-test.xml");
        if (fileName == null)
        {
            fileName = FSUtils.loadFromClasspath("pulga.xml");
        }
        return fileName;
    }
}