package br.com.ibnetwork.xingu.utils.type;

import java.util.Map;

public interface ObjectPopulator
{
	void populate(Object obj, Map<String, String> map)
		throws Exception;
}
