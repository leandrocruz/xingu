package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class DoubleFastCloner
	implements FastCloner<Double>
{
	@Override
	public Double clone(CloningContext ctx, Double original, Cloner cloner)
	{
		return original.doubleValue();
	}
}
