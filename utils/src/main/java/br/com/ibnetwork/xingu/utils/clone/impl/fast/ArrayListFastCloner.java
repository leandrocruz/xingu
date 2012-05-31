package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.ArrayList;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class ArrayListFastCloner
	implements FastCloner<ArrayList<?>>
{

	@Override
	public ArrayList<?> clone(CloningContext ctx, ArrayList<?> original, Cloner cloner)
	{
		ctx.increment();
		int size = original.size();
		ArrayList<Object> result = new ArrayList<Object>(size);
		
		for (int i = 0; i < size; i++)
		{
			Object item = original.get(i);
			Object clone = cloner.deepCloneWithContext(ctx, item);
			result.add(i, clone);
		}
		ctx.decrement();
		return result;
	}
}
