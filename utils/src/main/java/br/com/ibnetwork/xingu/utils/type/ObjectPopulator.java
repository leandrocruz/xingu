package br.com.ibnetwork.xingu.utils.type;

import br.com.ibnetwork.xingu.utils.collection.FluidMap;

public interface ObjectPopulator
{
	void populate(Object obj, FluidMap<String> map)
		throws Exception;
}
