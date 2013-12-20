package xingu.http.client.impl;

import java.io.File;

import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;

public interface CommandLineBuilder
{
	String name();

	String buildLine(HttpRequest request, File file);

	<T> HttpResponse<T> responseFrom(HttpRequest req, File file)
		throws Exception;
}
