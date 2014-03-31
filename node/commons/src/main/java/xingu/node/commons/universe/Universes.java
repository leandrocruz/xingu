package xingu.node.commons.universe;

public interface Universes
{
	void register(Universe universe);

	Universe byId(String id);

	Universe universeFor(Object obj);
}
