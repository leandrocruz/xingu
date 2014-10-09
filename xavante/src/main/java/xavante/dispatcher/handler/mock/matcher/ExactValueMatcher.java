package xavante.dispatcher.handler.mock.matcher;

import java.util.Iterator;
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

	@Override
	public String getMatch(List<String> values)
	{
		Iterator<String> it = values.iterator();
		while(it.hasNext())
		{
			String value = it.next();
			if(this.value.equals(value))
			{
				it.remove();
				return value;
			}
		}
		return null;
	}
}
