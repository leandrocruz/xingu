package xingu.http.client.mock;

import org.mockito.ArgumentMatcher;

public class ExactUrlMatcher
	extends ArgumentMatcher<String>
	implements UrlMatcher
{

	private String	uri;

	public ExactUrlMatcher(String uri)
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
		return uri.equals(other);
	}

	@Override
	public String getUri()
	{
		return uri;
	}
}
