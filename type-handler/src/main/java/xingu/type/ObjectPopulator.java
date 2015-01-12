package xingu.type;

import xingu.utils.collection.FluidMap;

public interface ObjectPopulator
{
	void populate(Object obj, FluidMap<String> map)
		throws Exception;
}
