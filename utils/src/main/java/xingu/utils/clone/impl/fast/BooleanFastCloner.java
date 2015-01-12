package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class BooleanFastCloner
	implements FastCloner<Boolean>
{
	@Override
	public Boolean clone(CloningContext ctx, Boolean original, Cloner cloner)
	{
		return original.booleanValue();
	}
}
