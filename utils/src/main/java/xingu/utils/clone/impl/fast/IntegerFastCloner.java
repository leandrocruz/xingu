package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class IntegerFastCloner
	implements FastCloner<Integer>
{
	@Override
	public Integer clone(CloningContext ctx, Integer original, Cloner cloner)
	{
		return original.intValue();
	}
}
