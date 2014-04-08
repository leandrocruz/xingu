package xingu.node.commons.sandbox;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;

public interface Sandbox
{
	String SYSTEM = "system";
	
	String id();
	
	NamedClassLoader classLoader();
	
	Container container();
	
	Object get(String component)
		throws Exception;
}
