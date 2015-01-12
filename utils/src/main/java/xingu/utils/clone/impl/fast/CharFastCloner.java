package xingu.utils.clone.impl.fast;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class CharFastCloner
	implements FastCloner<Character>
{
	@Override
	public Character clone(CloningContext ctx, Character original, Cloner cloner)
	{
		return original.charValue();
	}
}
