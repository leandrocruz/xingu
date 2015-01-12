package xingu.utils.clone.impl.fast;

import java.util.Date;

import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;

public class DateFastCloner
	implements FastCloner<Date>
{

	@Override
	public Date clone(CloningContext ctx, Date original, Cloner cloner)
	{
		return new Date(original.getTime());
	}
}
