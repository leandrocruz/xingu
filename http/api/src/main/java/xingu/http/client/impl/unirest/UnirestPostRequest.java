package xingu.http.client.impl.unirest;

import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

public class UnirestPostRequest
	implements HttpRequest
{

	private HttpRequestWithBody	req;

	public UnirestPostRequest(HttpRequestWithBody req)
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
