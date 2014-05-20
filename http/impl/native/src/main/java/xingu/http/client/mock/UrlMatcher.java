package xingu.http.client.mock;

import org.hamcrest.Matcher;

public interface UrlMatcher
	extends Matcher<String>
{
	String getUri();
}
