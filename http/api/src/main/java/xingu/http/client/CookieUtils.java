package xingu.http.client;

import java.util.Map;
import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;

import xingu.http.client.impl.CookiesImpl;

public class CookieUtils
{
	public static final Cookie[] EMPTY = new Cookie[]{};

	public static Cookies getCookies(HttpResponse<?> res)
	{
		Map<String, String> headers = res.getHeaders();
		String      header          = headers.get("set-cookie");
		if(header == null)
		{
			return null;
		}
		Set<Cookie> cookies         = new CookieDecoder().decode(header);
		return new CookiesImpl(cookies);
	}

	public static String getCookieNameAndValue(Cookie cookie)
	{
		return cookie.getName() + "=" + cookie.getValue();
	}
}