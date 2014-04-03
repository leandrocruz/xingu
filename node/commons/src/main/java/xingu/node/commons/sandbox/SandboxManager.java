package xingu.node.commons.sandbox;

public interface SandboxManager
{
	void register(Sandbox sandbox);

	Sandbox byId(String id);

	Sandbox sandboxFor(Object obj);
}
