package xingu.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;

public interface HttpResponse<T>
{
	int getCode();

	T getBody();

	InputStream getRawBody();
	
	NameValue[] getHeaders();

	String getHeader(String name);

	Document getDocument()
		throws IOException;
	
	Document getDocument(String charset)
		throws IOException;
}
