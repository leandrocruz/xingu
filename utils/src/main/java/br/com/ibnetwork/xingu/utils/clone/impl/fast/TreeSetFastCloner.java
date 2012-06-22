package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.Comparator;
import java.util.TreeSet;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class TreeSetFastCloner
	implements FastCloner<TreeSet<Object>>
{
	@Override
	public TreeSet<Object> clone(CloningContext ctx, TreeSet<Object> original, Cloner cloner)
	{
		ctx.increment();
		Comparator<? super Object> comparator = original.comparator();
		Comparator<? super Object> clonedComparator = cloner.deepCloneWithContext(ctx, comparator);
		TreeSet<Object> result = new TreeSet<Object>(clonedComparator);
		for(Object item : original)
		{
			Object clone = cloner.deepCloneWithContext(ctx, item);
			result.add(clone);
		}
		ctx.decrement();
		return result;
	}
}
