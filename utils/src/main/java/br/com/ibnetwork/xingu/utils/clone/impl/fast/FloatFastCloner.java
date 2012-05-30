package br.com.ibnetwork.xingu.utils.clone.impl.fast;

import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;

public class FloatFastCloner
	implements FastCloner<Float>
{
	@Override
	public Float clone(Float original, Cloner cloner)
	{
		return original.floatValue();
	}
}
