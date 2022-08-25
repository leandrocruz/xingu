package xingu.http.client.impl;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;

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
		return contentType != null && (contentType.toLowerCase().startsWith("text/html") || contentType.toLowerCase().startsWith("text/xml"));
	}

	@Override
	public Document getDocument(boolean lenient, String charset)
		throws IOException
	{
		if(lenient || isDocument())
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
		else
		{
			throw new IOException("Not a document '" + getHeader("Content-Type") + "'");
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