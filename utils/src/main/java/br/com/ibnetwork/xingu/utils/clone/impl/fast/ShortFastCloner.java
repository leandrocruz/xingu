package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class ShortFastCloner
	implements FastCloner<Short>
{
	@Override
	public Short clone(Short original, Cloner cloner)
	{
		return original.shortValue();
	}
}
