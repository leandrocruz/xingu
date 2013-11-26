package xingu.http.client;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface HttpResponse<T>
{
	int getCode();

	T getBody();

	Header[] getHeaders();

	String getHeader(String name);

	Document getDocument()
		throws IOException;
	
	Document getDocument(String charset)
		throws IOException;
}
