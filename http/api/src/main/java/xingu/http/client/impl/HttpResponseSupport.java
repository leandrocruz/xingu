package xingu.http.client.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import xingu.http.client.HttpResponse;

public abstract class HttpResponseSupport<T>
	implements HttpResponse<T>
{

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
			String url = getUrl();
			return Jsoup.parse(is, charset, url);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}

	protected abstract String getUrl();

	public abstract InputStream getRawBody();

}
