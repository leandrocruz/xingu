package xingu.http.client.impl;

import xingu.http.client.HttpRequest;
import xingu.http.client.impl.HttpClientSupport;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class UnirestHttpClient
	extends HttpClientSupport
{
	@Override
	public HttpRequest get(String uri)
	{
		GetRequest req = Unirest.get(uri);
		return new UnirestGetRequest(req);
	}

	@Override
	public HttpRequest post(String uri)
	{
		HttpRequestWithBody req = Unirest.post(uri);
		return new UnirestPostRequest(req);
	}
}