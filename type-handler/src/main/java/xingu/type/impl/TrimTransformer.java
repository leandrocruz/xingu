package xingu.type.impl;

import org.apache.commons.lang3.StringUtils;

import xingu.type.Transformer;

public class TrimTransformer
	implements Transformer
{
	@Override
	public String transform(String value)
	{
		return StringUtils.trim(value);
	}
}
