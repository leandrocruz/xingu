package xingu.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;

public interface HttpResponse
{
	int getCode();

	InputStream getRawBody()
		throws Exception;
	
	NameValue[] getHeaders();

	String getHeader(String name);

	boolean isDocument();

	Document getDocument()
		throws Exception;
	
	Document getDocument(String charset)
		throws Exception;

	File bodyAsFile()
		throws Exception;

	@Deprecated
	String getBody(String charset)
		throws Exception;

	@Deprecated
	String getBody() // ISO-8859-1
		throws Exception;
}
