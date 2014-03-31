package xingu.node.commons.sandbox;

public interface SanbdoxManager
{
	void register(Sandbox sandbox);

	Sandbox byId(String id);

	Sandbox sandboxFor(Object obj);
}
