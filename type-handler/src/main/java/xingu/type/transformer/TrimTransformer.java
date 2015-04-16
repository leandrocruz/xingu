package xingu.type.transformer;

import org.apache.commons.lang3.StringUtils;

public class TrimTransformer
	implements Transformer
{
	@Override
	public String transform(String value)
	{
		return StringUtils.trim(value);
	}
}
