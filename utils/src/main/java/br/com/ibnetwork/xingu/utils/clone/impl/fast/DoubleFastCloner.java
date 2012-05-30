package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class DoubleFastCloner
	implements FastCloner<Double>
{
	@Override
	public Double clone(Double original, Cloner cloner)
	{
		return original.doubleValue();
	}
}
