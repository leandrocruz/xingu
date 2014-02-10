package br.com.ibnetwork.xingu.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
        return getContainer(new File(fileName), start);
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

    public static Container getContainer(File file)
    	throws Exception
    {
    	return getContainer(file, true);
    }

    public static Container getContainer(File file, boolean start)
    	throws Exception
    {
        if(container != null)
        {
            return container;
        }
        container = createContainer(file);
        container.configure();
        if(start)
        {
        	container.start();
        }
        return container;
    }

    public static Container createContainer(File file)
    	throws Exception
    {
    	return createContainer(new FileInputStream(file));
	}

    public static Container createContainer(InputStream is)
    	throws Exception
    {
    	return new Pulga(is);
    }
}
