package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class FloatFastCloner
	implements FastCloner<Float>
{
	@Override
	public Float clone(CloningContext ctx, Float original, Cloner cloner)
	{
		return original.floatValue();
	}
}
