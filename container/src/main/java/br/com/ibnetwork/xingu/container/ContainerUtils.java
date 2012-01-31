package br.com.ibnetwork.xingu.container;

import br.com.ibnetwork.xingu.container.impl.Pulga;
import br.com.ibnetwork.xingu.utils.FSUtils;

public class ContainerUtils
{
    private static Container container;
    
    static final String XINGU_CONTAINER_FILE = "XINGU_CONTAINER_FILE";
    
    public static void reset()
    {
        if(container == null)
        {
            return;
        }
        container.stop();
        container = null;
    }

    public static Container getContainer()
    	throws Exception
    {
    	return getContainer(true);
    }
    
    public static Container getContainer(boolean start)
        throws Exception
    {
        if(container != null)
        {
            return container;
        }
        String fileName = getFileName();
        return getContainer(fileName, start);
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

    public static Container getContainer(String fileName)
    	throws Exception
    {
    	return getContainer(fileName, true);
    }

    public static Container getContainer(String fileName, boolean start)
    	throws Exception
    {
        if(container != null)
        {
            return container;
        }
        container = createContainer(fileName);
        container.configure();
        if(start)
        {
        	container.start();
        }
        return container;
    }
    
    public static Container createContainer(String fileName)
    	throws Exception
    {
        Pulga pulga = new Pulga(fileName);
        return pulga;
    }
}
