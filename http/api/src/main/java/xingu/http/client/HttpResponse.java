package xingu.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;

public interface HttpResponse
{
	int getCode();

	InputStream getRawBody()
		throws IOException;
	
	NameValue[] getHeaders();

	String getHeader(String name);

	boolean isDocument();

	Document getDocument()
		throws IOException;
	
	Document getDocument(String charset)
		throws IOException;

	File bodyAsFile()
		throws IOException;

	@Deprecated
	String getBody(String charset)
		throws IOException;

	@Deprecated
	String getBody() // ISO-8859-1
		throws IOException;
}
