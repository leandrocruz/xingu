package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class CharFastCloner
	implements FastCloner<Character>
{
	@Override
	public Character clone(Character original, Cloner cloner)
	{
		return original.charValue();
	}
}
