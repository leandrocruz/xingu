package xingu.node.commons.universe.impl;

import java.util.HashMap;
import java.util.Map;

import xingu.node.commons.universe.Universe;
import xingu.node.commons.universe.Universes;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class UniversesImpl
	implements Universes
{
	private Map<String, Universe> universeById = new HashMap<String, Universe>();

	@Override
	public Universe universeFor(Object obj)
	{
		return byId(Universe.SYSTEM);
	}

	@Override
	public Universe byId(String id)
	{
		return universeById.get(id);
	}

	@Override
	public void register(Universe universe)
	{
		String   id  = universe.id();
		Universe old = universeById.get(id);
		if(old != null)
		{
			throw new NotImplementedYet("Universes can't be replaced");
		}
		universeById.put(id, universe);
	}
}
