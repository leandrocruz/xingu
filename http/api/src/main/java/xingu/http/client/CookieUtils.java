package xingu.http.client;

import java.util.Set;

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
		StringBuffer  sb      = new StringBuffer();
		NameValue[]   headers = res.getHeaders();

		for(NameValue header : headers)
		{
			String name = header.getName();
			if("set-cookie".equalsIgnoreCase(name))
			{
				String value = header.getValue();
				sb.append(value).append(";");
			}
		}

		String      buffer  = sb.toString();
		Set<Cookie> decoded = decoder.decode(buffer);
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
		Set<Cookie> currentSet = first.getBuffer();
		Set<Cookie> toMergeSet = second.getBuffer();
		for(Cookie cookie : toMergeSet)
		{
			String name    = cookie.getName();
			Cookie current = first.byName(name);
			if(current == null)
			{
				currentSet.add(cookie);
			}
			else
			{
				currentSet.remove(current);
				currentSet.add(cookie);
			}
		}
		return new CookiesImpl(currentSet);
	}
}