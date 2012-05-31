package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class LongFastCloner
	implements FastCloner<Long>
{
	@Override
	public Long clone(CloningContext ctx, Long original, Cloner cloner)
	{
		return original.longValue();
	}
}
