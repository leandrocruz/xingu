package xingu.http.client.impl;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.netty.http.HttpUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class HttpResponseImpl
	implements HttpResponse
{
	private static final NameValue[] EMPTY = new NameValue[]{};

	private String		uri;

	private int			code;

	private List<NameValue> headers;

	private File body;

	@Override
	public String getBody()
		throws IOException
	{
		return getBody(HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
	}

	@Override
	public String getBody(String charset)
		throws IOException
	{
		InputStream rawBody = getRawBody();
		return IOUtils.toString(rawBody, Charset.forName(charset));
	}

	@Override
	public boolean isDocument()
	{
		String contentType = getHeader("Content-Type");
		return contentType != null && contentType.toLowerCase().startsWith("text/html");
	}

	@Override
	public Document getDocument()
		throws IOException
	{
		return getDocument(HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
	}
	
	@Override
	public Document getDocument(String charset)
		throws IOException
	{
		if(!isDocument())
			throw new IOException("Not a document '" + getHeader("Content-Type") + "'");

		InputStream is = null;
		try
		{
			is         = getRawBody();
			String url = getUri();
			return Jsoup.parse(is, charset, url);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
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

	@Override public InputStream getRawBody()
		throws IOException
	{
		return new BufferedInputStream(new FileInputStream(body));
	}

	@Override public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public String getUri()
	{
		return uri;
	}

	@Override public NameValue[] getHeaders()
	{
		return headers.toArray(EMPTY);
	}

	public void setHeaders(List<NameValue> headers)
	{
		this.headers = headers;
	}

	public void setBody(File file)
	{
		this.body = file;
	}

	@Override
	public File bodyAsFile()
		throws IOException
	{
		return body;
	}
}