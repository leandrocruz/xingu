package xingu.http.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;

import xingu.http.client.impl.CookiesImpl;

public class CookieUtils
{
	public static final Cookie[] EMPTY = new Cookie[]{};

	private static CookieDecoder decoder = new CookieDecoder();

	public static Cookies toCookies(String input)
	{
		Set<Cookie>   decoded = decoder.decode(input);
		return new CookiesImpl(decoded);
	}
	
	public static Cookies getCookies(HttpResponse res)
	{

		/*
		 * Sometimes, multiple set-cookie headers are sent for the same cookie.
		 * We need to parse all 'set-cookie' headers, but store only the last
		 */
		Map<String, Cookie> cookieByName = new HashMap<>();

		NameValue[] headers = res.getHeaders();
		for(NameValue header : headers)
		{
			String name = header.getName();
			if("set-cookie".equalsIgnoreCase(name))
			{
				String value = header.getValue();
				Set<Cookie> cookies = decoder.decode(value);
				for(Cookie cookie : cookies)
				{
					cookieByName.put(cookie.getName(), cookie);
				}
			}
		}

		Set<Cookie> decoded = new TreeSet<>();
		for(String name : cookieByName.keySet())
		{
			decoded.add(cookieByName.get(name));
		}
		
		return new CookiesImpl(decoded);
	}

	public static Cookie replaceCookie(HttpResponse resp, Cookie old)
	{
		String name = old.getName();
		Cookie cookie = getCookies(resp).byName(name);
		if(cookie == null)
		{
			return old;
		}
		return cookie;
	}

	public static String getCookieNameAndValue(Cookie cookie)
	{
		return cookie.getName() + "=" + cookie.getValue();
	}

	public static Cookies mergeCookies(Cookies first, Cookies second)
	{
		Cookies     result    = new CookiesImpl();
		Set<Cookie> merged    = result.getBuffer();
		Set<Cookie> firstSet  = first.getBuffer();
		Set<Cookie> secondSet = second.getBuffer();

		merged.addAll(firstSet);
		
		for(Cookie onSecond : secondSet)
		{
			String name    = onSecond.getName();
			Cookie onFirst = result.byName(name);
			if(onFirst != null)
			{
				merged.remove(onFirst);
			}
			merged.add(onSecond);
		}
		return new CookiesImpl(merged);
	}
}