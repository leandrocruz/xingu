package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.HashSet;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class HashSetFastCloner
	implements FastCloner<HashSet<Object>>
{
	@Override
	public HashSet<Object> clone(CloningContext ctx, HashSet<Object> original, Cloner cloner)
	{
		ctx.increment();
		HashSet<Object> result = new HashSet<Object>();
		for(Object item : original)
		{
			Object clone = cloner.deepCloneWithContext(ctx, item);
			result.add(clone);
		}
		ctx.decrement();
		return result;
	}
}
