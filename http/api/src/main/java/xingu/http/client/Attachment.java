package xingu.http.client;

import java.io.File;

public interface Attachment
{
	String getName(); /* The HTTP Field Name */
	
	String getFileName();
	
	File getFile();
	
	String getMime();
}
