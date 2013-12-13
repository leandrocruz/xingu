package xingu.http.client.impl;

import java.io.InputStream;
import java.util.Map;

import xingu.http.client.NameValue;
import xingu.http.client.impl.NameValueImpl;
import xingu.http.client.impl.HttpResponseSupport;

public class UnirestHttpResponse<T>
	extends HttpResponseSupport<T>
{
	private com.mashape.unirest.request.HttpRequest		req;

	private com.mashape.unirest.http.HttpResponse<T>	res;

	public UnirestHttpResponse(com.mashape.unirest.request.HttpRequest req, com.mashape.unirest.http.HttpResponse<T> res)
	{
		this.res = res;
		this.req = req;
	}

	@Override
	public T getBody()
	{
		return res.getBody();
	}

	@Override
	public NameValue[] getHeaders()
	{
		Map<String, String> headers = res.getHeaders();
		NameValue[] result = new NameValue[headers.size()];
		int i = 0;
		for(String name : headers.keySet())
		{
			String value = headers.get(name);
			result[i++] = new NameValueImpl(name, value);
		}
		return result;
	}

	@Override
	public int getCode()
	{
		return res.getCode();
	}

	@Override
	public String getHeader(String name)
	{
		Map<String, String> headers = res.getHeaders();
		return headers.get(name);
	}

	@Override
	protected String getUrl()
	{
		return req.getUrl();
	}

	@Override
	public InputStream getRawBody()
	{
		return res.getRawBody();
	}
}