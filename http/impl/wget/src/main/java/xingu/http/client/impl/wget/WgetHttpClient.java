package xingu.http.client.impl.wget;

import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;

public class WgetHttpClient
	implements HttpClient
{

	@Override
	public HttpRequest get(String uri)
		throws HttpException
	{
		return null;
	}

	@Override
	public HttpRequest post(String uri)
		throws HttpException
	{
		return null;
	}

	@Override
	public Cookies getCookies(String uri)
		throws HttpException
	{
		return null;
	}

}
