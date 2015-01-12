package xingu.node.commons.sandbox;

import xingu.container.Container;
import xingu.utils.classloader.NamedClassLoader;

public interface Sandbox
{
	String SYSTEM = "system";
	
	String id();
	
	NamedClassLoader classLoader();
	
	Container container();
	
	Object get(String component)
		throws Exception;
}
