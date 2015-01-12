package xingu.utils.clone.impl.fast;

import java.util.HashMap;
import java.util.Set;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class HashMapFastCloner
	implements FastCloner<HashMap<?, ?>>
{

	@Override
	public HashMap<?, ?> clone(CloningContext ctx, HashMap<?, ?> original, Cloner cloner)
	{
		ctx.clearName();
		ctx.increment();
		HashMap<Object, Object> result = new HashMap<Object, Object>(original.size());
		Set<?> keys = original.keySet();
		for (Object key : keys)
		{
			Object value = original.get(key);
			ctx.setName("[k]");
			Object keyClone = cloner.deepCloneWithContext(ctx, key);
			ctx.setName("[v]");
			Object valueClone = cloner.deepCloneWithContext(ctx, value);
			result.put(keyClone, valueClone);
		}
		ctx.decrement();
		ctx.clearName();
		return result;
	}
}
