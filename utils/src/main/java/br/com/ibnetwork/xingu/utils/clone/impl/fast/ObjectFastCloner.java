package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class ObjectFastCloner
	implements FastCloner<Object>
{
	@Override
	public Object clone(CloningContext ctx, Object original, Cloner cloner)
	{
		return new Object();
	}
}
