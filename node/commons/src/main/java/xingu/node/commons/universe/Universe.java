package xingu.node.commons.universe;

import br.com.ibnetwork.xingu.container.Container;

public interface Universe
{
	String SYSTEM = "system";
	
	String id();
	
	ClassLoader classLoader();
	
	Container container();
}
