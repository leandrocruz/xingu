package xavante.dispatcher.handler.mock.matcher;

import java.util.List;

public class ExactValueMatcher
	implements ValueMatcher
{
	private String	value;

	public ExactValueMatcher(String value)
	{
		this.value = value;
	}

	@Override
	public boolean matchAny(List<String> values)
	{
		return values.contains(this.value);
	}
}
