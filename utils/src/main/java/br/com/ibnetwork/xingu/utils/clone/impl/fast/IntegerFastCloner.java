package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class IntegerFastCloner
	implements FastCloner<Integer>
{
	@Override
	public Integer clone(CloningContext ctx, Integer original, Cloner cloner)
	{
		return original.intValue();
	}
}
