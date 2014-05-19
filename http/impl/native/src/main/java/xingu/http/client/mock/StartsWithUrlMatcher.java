package xingu.http.client.mock;

import org.mockito.ArgumentMatcher;

public class StartsWithUrlMatcher
	extends ArgumentMatcher<String>
	implements UrlMatcher
{
	private String	uri;

	public StartsWithUrlMatcher(String uri)
	{
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
		return other.startsWith(uri);
	}

	@Override
	public String getUri()
	{
		return uri;
	}
}
