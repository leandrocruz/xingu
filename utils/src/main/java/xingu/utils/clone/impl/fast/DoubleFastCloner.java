package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class DoubleFastCloner
	implements FastCloner<Double>
{
	@Override
	public Double clone(CloningContext ctx, Double original, Cloner cloner)
	{
		return original.doubleValue();
	}
}
