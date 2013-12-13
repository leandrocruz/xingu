package xingu.http.client.impl;

import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class UnirestGetRequest
	extends HttpRequestSupport
{
	private GetRequest	req;

	public UnirestGetRequest(GetRequest req)
	{
		this.req = req;
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		req.header(name, value);
		return this;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		req.field(name, value);
		return this;
	}

	@Override
	public HttpResponse<String> asString()
		throws HttpException
	{
		com.mashape.unirest.http.HttpResponse<String> res;
		try
		{
			res = req.asString();
		}
		catch(UnirestException e)
		{
			throw new HttpException(e);
		}
		return new UnirestHttpResponse<String>(req, res);
	}

	@Override
	public String getUri()
	{
		return req.getUrl();
	}
}
