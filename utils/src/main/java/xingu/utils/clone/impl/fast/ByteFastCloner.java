package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class ByteFastCloner
	implements FastCloner<Byte>
{
	@Override
	public Byte clone(CloningContext ctx, Byte original, Cloner cloner)
	{
		return original.byteValue();
	}
}
