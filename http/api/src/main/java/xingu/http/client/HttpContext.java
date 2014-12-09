package xingu.http.client;

import java.io.File;

import org.slf4j.Logger;

public interface HttpContext
{
	String getRequestId();

	File getRootDirectory();

	Logger getLogger();
}
