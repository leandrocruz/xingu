package xingu.http.client.impl.unirest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import xingu.http.client.HttpResponse;

public class UnirestHttpResponse<T>
	implements HttpResponse<T>
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
	public Map<String, String> getHeaders()
	{
		return res.getHeaders();
	}

	@Override
	public int getCode()
	{
		return res.getCode();
	}

	@Override
	public String getHeader(String name)
	{
		Map<String, String> headers = getHeaders();
		return headers.get(name);
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
			is         = res.getRawBody();
			String url = req.getUrl();
			return Jsoup.parse(is, charset, url);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
}
