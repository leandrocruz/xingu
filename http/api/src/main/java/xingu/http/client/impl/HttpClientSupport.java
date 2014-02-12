package xingu.http.client.impl;

import xingu.http.client.CookieUtils;
import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;

public abstract class HttpClientSupport
	implements HttpClient
{
	@Override
	public Cookies getCookies(String uri)
		throws HttpException
	{
		xingu.http.client.HttpResponse res = get(uri)
				.header("Accept", "text/html")
				.header("Accept-Charset", "ISO-8859-1,utf-8")
				.exec();
		return CookieUtils.getCookies(res);
	}
}