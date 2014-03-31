package xingu.node.commons.sandbox;

import br.com.ibnetwork.xingu.container.Container;

public interface Sandbox
{
	String SYSTEM = "system";
	
	String id();
	
	ClassLoader classLoader();
	
	Container container();
	
	Object get(String component)
		throws Exception;
}
