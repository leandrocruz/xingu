package xingu.http.client.impl;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.impl.SimpleHttpRequest;
import xingu.lang.NotImplementedYet;

public class SimpleHttpClient
	implements HttpClient
{
	@Inject
	private Factory factory;
	
	@Override
	public HttpRequest get(String uri)
		throws HttpException
	{
		return factory.create(SimpleHttpRequest.class, uri, "GET");
	}

	@Override
	public HttpRequest post(String uri)
		throws HttpException
	{
		return factory.create(SimpleHttpRequest.class, uri, "POST");
	}

	@Override
	public Cookies getCookies(String uri)
		throws HttpException
	{
		throw new NotImplementedYet();
	}
}
