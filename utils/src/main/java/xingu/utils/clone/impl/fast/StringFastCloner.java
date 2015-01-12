package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class StringFastCloner
	implements FastCloner<String>
{
	@Override
	public String clone(CloningContext ctx, String original, Cloner cloner)
	{
		return new String(original);
	}
}
