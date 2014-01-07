package xingu.http.client;

import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;

import xingu.http.client.impl.CookiesImpl;

public class CookieUtils
{
	public static final Cookie[] EMPTY = new Cookie[]{};

	public static Cookies getCookies(HttpResponse<?> res)
	{
		StringBuffer  sb      = new StringBuffer();
		NameValue[]   headers = res.getHeaders();
		CookieDecoder decoder = new CookieDecoder();

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

	public static String getCookieNameAndValue(Cookie cookie)
	{
		return cookie.getName() + "=" + cookie.getValue();
	}
}