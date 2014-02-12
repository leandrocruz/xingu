package xingu.http.client.impl;

import java.io.InputStream;

import xingu.http.client.NameValue;
import xingu.http.client.impl.HttpResponseSupport;

public class SimpleHttpResponse
	extends HttpResponseSupport
{
	private int			code;

	private NameValue[]	headers;

	private String		uri;

	private String		body;

	private InputStream	raw;

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public NameValue[] getHeaders()
	{
		return headers;
	}

	@Override
	public String getHeader(String name)
	{
		for(NameValue h : headers)
		{
			if(h.getName().equals(name))
			{
				return h.getValue();
			}
		}
		return null;
	}

	@Override
	protected String getUrl()
	{
		return uri;
	}

	@Override
	public InputStream getRawBody()
	{
		return raw;
	}

	@Override
	public String getBody()
	{
		return body;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public void setHeaders(NameValue[] headers)
	{
		this.headers = headers;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public void setRawBody(InputStream is)
	{
		this.raw = is;
	}
}
