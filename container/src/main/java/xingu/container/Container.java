package xingu.container;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;

public interface Container
	extends Configurable
{
//	<T> T lookup(String string)
//		throws ContainerException;
//
//	<T> T lookup(String string, String key)
//		throws ContainerException;

	<T> T lookup(Class<T> clazz)
		throws ContainerException;

    <T> T lookup(Class<T> clazz, String key)
		throws ContainerException;

    boolean isResolved(Class<?> clazz, String key);
    
    void configure()
    	throws ContainerException;

    void start()
    	throws ContainerException;
    
    void stop()
    	throws ContainerException;

    void startLifecycle(Object component, Configuration conf) 
        throws Exception;

    void stopLifecycle(Object obj) 
        throws Exception;

    Binder binder();
}