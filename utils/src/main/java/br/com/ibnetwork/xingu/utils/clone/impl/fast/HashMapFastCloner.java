package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.HashMap;
import java.util.Set;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class HashMapFastCloner
	implements FastCloner<HashMap<?, ?>>
{

	@Override
	public HashMap<?, ?> clone(HashMap<?, ?> original, Cloner cloner)
	{
		HashMap<Object, Object> result = new HashMap<Object, Object>(original.size());
		Set<?> keys = original.keySet();
		for (Object key : keys)
		{
			Object value = original.get(key);
			Object keyClone = cloner.deepClone(key);
			Object valueClone = cloner.deepClone(value);
			result.put(keyClone, valueClone);
		}
		return result;
	}
}
