package xingu.http.client.impl;

import java.io.InputStream;

import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.NameValue;

public class ApacheHttpResponse
	extends HttpResponseSupport
{
	private HttpUriRequest					req;

	private org.apache.http.HttpResponse	res;

	private int								code;

	private NameValue[]						headers;

	private String							body;

	private InputStream						rawBody;

	public ApacheHttpResponse(HttpUriRequest req, org.apache.http.HttpResponse res)
	{
		this.req = req;
		this.res = res;
	}

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public String getBody()
	{
		return body;
	}

	@Override
	public NameValue[] getHeaders()
	{
		return headers;
	}

	@Override
	public String getHeader(String wanted)
	{
		for(NameValue h : headers)
		{
			String name = h.getName();
			if(wanted.equalsIgnoreCase(name))
			{
				return h.getValue();
			}
		}
		return null;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public void setHeaders(NameValue[] headers)
	{
		this.headers = headers;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	@Override
	protected String getUrl()
	{
		return req.getURI().toString();
	}

	@Override
	public InputStream getRawBody()
	{
		return rawBody;
	}

	public void setRawBody(InputStream is)
	{
		this.rawBody = is;
	}
}
