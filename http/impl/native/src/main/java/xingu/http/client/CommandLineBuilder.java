package xingu.http.client;

import java.io.File;
import java.util.List;

import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;

public interface CommandLineBuilder
{
	String name();

	List<String> buildLine(HttpRequest request, File file)
		throws Exception;

	HttpResponse responseFrom(HttpRequest req, File file)
		throws Exception;

	String join(List<String> cmd);
}
