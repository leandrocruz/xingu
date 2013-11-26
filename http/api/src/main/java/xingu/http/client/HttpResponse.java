package xingu.http.client;

import java.io.IOException;
import java.util.Map;

import org.jsoup.nodes.Document;

public interface HttpResponse<T>
{
	int getCode();

	T getBody();

	Map<String, String> getHeaders();

	String getHeader(String name);

	Document getDocument()
		throws IOException;
	
	Document getDocument(String charset)
		throws IOException;
}
