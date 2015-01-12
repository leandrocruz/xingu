package xingu.utils.clone.impl.fast;

import java.util.HashSet;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class HashSetFastCloner
	implements FastCloner<HashSet<Object>>
{
	@Override
	public HashSet<Object> clone(CloningContext ctx, HashSet<Object> original, Cloner cloner)
	{
		ctx.clearName();
		ctx.increment();
		HashSet<Object> result = new HashSet<Object>();
		for(Object item : original)
		{
			Object clone = cloner.deepCloneWithContext(ctx, item);
			result.add(clone);
		}
		ctx.decrement();
		ctx.clearName();
		return result;
	}
}
