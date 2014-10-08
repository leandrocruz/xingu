package xavante.dispatcher.handler.mock.matcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpValueMatcher
	implements ValueMatcher
{
	private String	value;

	private Pattern pattern;
	
	public RegexpValueMatcher(String value)
	{
		pattern = Pattern.compile(value);
		this.value = value;
	}

	@Override
	public boolean matchAny(List<String> values)
	{
		for(String value : values)
		{
			Matcher matcher = pattern.matcher(value);
			if(matcher.matches())
			{
				return true;
			}
		}
		return false;
	}
}
