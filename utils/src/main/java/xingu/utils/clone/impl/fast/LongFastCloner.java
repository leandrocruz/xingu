package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class LongFastCloner
	implements FastCloner<Long>
{
	@Override
	public Long clone(CloningContext ctx, Long original, Cloner cloner)
	{
		return original.longValue();
	}
}
