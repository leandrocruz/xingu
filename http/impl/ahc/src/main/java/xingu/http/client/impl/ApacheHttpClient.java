package xingu.http.client.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.impl.HttpClientSupport;

public class ApacheHttpClient
	extends HttpClientSupport 
	implements HttpClient
{
	@Override
	public HttpRequest get(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpGet(uri);
		return new ApacheRequest(req);
	}

	@Override
	public HttpRequest post(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpPost(uri);
		return new ApacheRequest(req);
	}
}
