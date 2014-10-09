package xavante.dispatcher.handler.mock.matcher;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpValueMatcher
	implements ValueMatcher
{
	private Pattern pattern;
	
	public RegexpValueMatcher(String value)
	{
		pattern = Pattern.compile(value);
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

	@Override
	public String getMatch(List<String> values)
	{
		Iterator<String> it = values.iterator();
		while(it.hasNext())
		{
			String value = it.next();
			Matcher matcher = pattern.matcher(value);
			if(matcher.matches())
			{
				it.remove();
				return value;
			}
		}
		return null;
	}
}