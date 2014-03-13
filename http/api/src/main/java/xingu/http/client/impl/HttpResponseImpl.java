package xingu.http.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.netty.http.HttpUtils;

public class HttpResponseImpl
	implements HttpResponse
{
	private String		uri;

	private int			code;

	private NameValue[]	headers;

	private InputStream	raw;

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
	public Document getDocument()
		throws IOException
	{
		return getDocument("ISO-8859-1");
	}

	@Override
	public Document getDocument(String charset)
		throws IOException
	{
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

	/* @formatter:off */
	@Override public int getCode(){return code;}
	public void setCode(int code){this.code = code;}
	public void setUri(String uri){this.uri = uri;}
	public String getUri(){return uri;}
	@Override public NameValue[] getHeaders(){return headers;}
	public void setHeaders(NameValue[] headers){this.headers = headers;}
	@Override public InputStream getRawBody(){return raw;}
	public void setRawBody(InputStream is){this.raw = is;}
	/* @formatter:on */
}