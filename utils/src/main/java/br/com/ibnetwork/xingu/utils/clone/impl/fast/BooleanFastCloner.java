package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class BooleanFastCloner
	implements FastCloner<Boolean>
{
	@Override
	public Boolean clone(Boolean original, Cloner cloner)
	{
		return original.booleanValue();
	}
}
