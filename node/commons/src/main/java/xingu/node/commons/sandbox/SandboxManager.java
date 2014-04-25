package xingu.node.commons.sandbox;

import java.util.Collection;

public interface SandboxManager
{
	Sandbox byId(String id);

	Sandbox sandboxFor(Object obj);
	
	Sandbox resolve(String id)
		throws Exception;
	
	Collection<Sandbox> getAll();
}