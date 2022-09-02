package xingu.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;
import xingu.netty.http.HttpUtils;

public interface HttpResponse
{
	int getCode();

	InputStream getRawBody()
		throws IOException;
	
	NameValue[] getHeaders();

	String getHeader(String name);

	boolean isDocument();

	default Document getDocument()
		throws IOException
	{
		return getDocument(true);
	}
	
	default Document getDocument(String charset)
		throws IOException
	{
		return getDocument(true, charset);
	}

	default Document getDocument(boolean lenient)
		throws IOException
	{
		return getDocument(lenient, HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
	}

	Document getDocument(boolean lenient, String charset)
		throws IOException;

	File bodyAsFile()
		throws IOException;

	default String getBody()
		throws IOException
	{
		return getBody(HttpUtils.DEFAULT_HTTP_CHARSET_NAME);
	}

	String getBody(String charset)
		throws IOException;
}