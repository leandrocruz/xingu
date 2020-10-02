package xingu.http.client.impl;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;

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
	
	@Override
	public HttpRequest put(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpPut(uri);
		return new ApacheRequest(req);
	}

	@Override
	public HttpRequest delete(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpDelete(uri);
		return new ApacheRequest(req);
	}

	@Override
	public HttpRequest options(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpOptions(uri);
		return new ApacheRequest(req);
	}

	@Override
	public HttpRequest head(String uri)
		throws HttpException
	{
		HttpUriRequest req = new HttpHead(uri);
		return new ApacheRequest(req);
	}
}
