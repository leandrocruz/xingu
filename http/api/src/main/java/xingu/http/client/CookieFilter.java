package xingu.http.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Predicate;

import org.jboss.netty.handler.codec.http.Cookie;

public class CookieFilter
{
	private Optional<String>  path 	 = Optional.empty();
	private Optional<String>  domain = Optional.empty();
	private Optional<Boolean> secure = Optional.empty();
	
	public static CookieFilter from(String uri)
		throws URISyntaxException
	{
		URI parser = new URI(uri);
		return new CookieFilter()
			.secure("https".equals(parser.getScheme()))
			.domain(parser.getHost())
			.path(parser.getPath());
	}

	public CookieFilter path(String path)
	{
		this.path = Optional.of(path);
		return this;
	}
	
	public CookieFilter domain(String domain)
	{
		this.domain = Optional.of(domain);
		return this;
	}

	public CookieFilter secure(boolean secure)
	{
		this.secure = Optional.of(secure);
		return this;
	}
	
	public CookieFilter build()
	{
		return this;		
	}

	public boolean filter(Cookie cookie)
	{
		boolean pathAllowed 	= check(cookie.getPath()   ,this::pathAllowed);
		boolean domainAllowed 	= check(cookie.getDomain() ,this::domainAllowed);
		boolean protocolAllowed = check(cookie.isSecure()  ,this::protocolAllowed);
		return pathAllowed && domainAllowed && protocolAllowed;
	}

	private <T> boolean check(T value, Predicate<T> predicate)
	{
		return value != null ? predicate.test(value) : true;
	}
	
	private boolean protocolAllowed(boolean value)
	{
		return this.secure.map(required -> value == required).orElse(true);
	}

	private boolean domainAllowed(String value)
	{
		//System.out.println("Cookie: " + value + ", Filter: " + this.domain);
		return this.domain.map(required -> required.indexOf(value) >= 0).orElse(true);
	}

	private boolean pathAllowed(String value)
	{
		//System.out.println("Cookie: " + value + ", Filter: " + this.path);
		return this.path.map(required -> required.indexOf(value) >= 0).orElse(true);
	}
}
