package xingu.node.commons.sandbox;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.utils.classloader.SimpleClassLoader;

public interface Sandbox
{
	String SYSTEM = "system";
	
	String id();
	
	SimpleClassLoader classLoader();
	
	Container container();
	
	Object get(String component)
		throws Exception;
}
