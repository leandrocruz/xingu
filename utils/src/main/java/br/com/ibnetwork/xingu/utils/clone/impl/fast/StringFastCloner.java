package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class StringFastCloner
	implements FastCloner<String>
{
	@Override
	public String clone(CloningContext ctx, String original, Cloner cloner)
	{
		return new String(original);
	}
}
