package xingu.utils.clone.impl.fast;

import java.util.ArrayList;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class ArrayListFastCloner
	implements FastCloner<ArrayList<?>>
{

	@Override
	public ArrayList<?> clone(CloningContext ctx, ArrayList<?> original, Cloner cloner)
	{
		ctx.clearName();
		ctx.increment();
		int size = original.size();
		ArrayList<Object> result = new ArrayList<Object>(size);
		
		for (int i = 0; i < size; i++)
		{
			Object item = original.get(i);
			ctx.setName("["+i+"]");
			Object clone = cloner.deepCloneWithContext(ctx, item);
			result.add(i, clone);
		}
		ctx.decrement();
		ctx.clearName();
		return result;
	}
}
