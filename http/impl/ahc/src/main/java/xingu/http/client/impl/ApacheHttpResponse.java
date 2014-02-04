package xingu.http.client.impl;

import java.io.InputStream;

import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.NameValue;
import xingu.http.client.impl.HttpResponseSupport;

public class ApacheHttpResponse<T>
	extends HttpResponseSupport<T>
{
	private HttpUriRequest					req;

	private org.apache.http.HttpResponse	res;

	private int								code;

	private NameValue[]						headers;

	private T								body;

	private InputStream	rawBody;

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
	public T getBody()
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

	public void setBody(T body)
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