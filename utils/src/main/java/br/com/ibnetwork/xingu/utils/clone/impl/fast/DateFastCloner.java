package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import java.util.Date;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class DateFastCloner
	implements FastCloner<Date>
{

	@Override
	public Date clone(CloningContext ctx, Date original, Cloner cloner)
	{
		return new Date(original.getTime());
	}
}
