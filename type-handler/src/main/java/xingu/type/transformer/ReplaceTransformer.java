package xingu.type.transformer;

public class ReplaceTransformer
	implements Transformer
{

	private String regexp;

	private String replacement;

	@Override
	public String transform(String value)
	{
		if(value == null)
		{
			return null;
		}
		return value.replaceAll(regexp, replacement);
	}

	public void setRegexp(String regexp){this.regexp = regexp;}
	public void setReplacement(String replacement){this.replacement = replacement;}
}
