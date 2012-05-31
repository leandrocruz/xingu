package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.HashMap;
import java.util.Set;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class HashMapFastCloner
	implements FastCloner<HashMap<?, ?>>
{

	@Override
	public HashMap<?, ?> clone(CloningContext ctx, HashMap<?, ?> original, Cloner cloner)
	{
		ctx.increment();
		HashMap<Object, Object> result = new HashMap<Object, Object>(original.size());
		Set<?> keys = original.keySet();
		for (Object key : keys)
		{
			Object value = original.get(key);
			Object keyClone = cloner.deepCloneWithContext(ctx, key);
			Object valueClone = cloner.deepCloneWithContext(ctx, value);
			result.put(keyClone, valueClone);
		}
		ctx.decrement();
		return result;
	}
}
