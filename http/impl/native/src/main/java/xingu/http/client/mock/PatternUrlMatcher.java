package xingu.http.client.mock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.ArgumentMatcher;

public class PatternUrlMatcher
	extends ArgumentMatcher<String>
	implements UrlMatcher
{
	private Pattern p;
	
	private String	uri;

	public PatternUrlMatcher(String uri)
	{
		p = Pattern.compile(uri);
		this.uri = uri;
	}

	@Override
	public boolean matches(Object arg)
	{
		if(!(arg instanceof String))
		{
			return false;
		}
		String other = (String) arg;

		Matcher m = p.matcher(other);
		boolean matches = m.matches();
		return matches;
	}

	@Override
	public String getUri()
	{
		return uri;
	}
}
