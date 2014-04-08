package xingu.node.commons.sandbox;

import java.util.Collection;

public interface SandboxManager
{
	void register(Sandbox sandbox);

	Sandbox byId(String id);

	Sandbox sandboxFor(Object obj);
	
	Collection<Sandbox> getAll();
}