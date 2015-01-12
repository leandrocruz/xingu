package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class ShortFastCloner
	implements FastCloner<Short>
{
	@Override
	public Short clone(CloningContext ctx, Short original, Cloner cloner)
	{
		return original.shortValue();
	}
}
