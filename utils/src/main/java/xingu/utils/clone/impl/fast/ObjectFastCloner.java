package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class ObjectFastCloner
	implements FastCloner<Object>
{
	@Override
	public Object clone(CloningContext ctx, Object original, Cloner cloner)
	{
		return new Object();
	}
}
