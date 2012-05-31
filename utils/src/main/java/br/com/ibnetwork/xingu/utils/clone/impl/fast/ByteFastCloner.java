package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class ByteFastCloner
	implements FastCloner<Byte>
{
	@Override
	public Byte clone(CloningContext ctx, Byte original, Cloner cloner)
	{
		return original.byteValue();
	}
}
