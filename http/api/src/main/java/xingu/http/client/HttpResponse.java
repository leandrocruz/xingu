package xingu.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;

public interface HttpResponse
{
	int getCode();

	String getBody(String charset)
		throws IOException;
	
	String getBody() // ISO-8859-1
		throws IOException; 
	
	InputStream getRawBody();
	
	NameValue[] getHeaders();

	String getHeader(String name);

	Document getDocument()
		throws IOException;
	
	Document getDocument(String charset)
		throws IOException;
}
