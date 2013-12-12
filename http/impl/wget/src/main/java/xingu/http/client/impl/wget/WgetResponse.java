package xingu.http.client.impl.wget;

import java.io.File;
import java.io.InputStream;

import xingu.http.client.Header;
import xingu.http.client.impl.HttpResponseSupport;

public class WgetResponse<T>
	extends HttpResponseSupport<T>
{
	private File		file;

	private int			code;

	private Header[]	headers;

	private String		uri;

	private String		body;

	private InputStream	raw;

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public Header[] getHeaders()
	{
		return headers;
	}

	@Override
	public String getHeader(String name)
	{
		for(Header h : headers)
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
	public T getBody()
	{
		return (T) body;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public void setHeaders(Header[] headers)
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
