package xingu.http.client.impl;

import java.io.File;
import java.util.Objects;

import xingu.http.client.Attachment;

public class AttachmentImpl
	implements Attachment
{
	private String	name; /* the field name */

	private String	fileName;

	private File	file;

	private String	mime;

	public AttachmentImpl(String name, File file)
	{
		this(name, file, null);
	}

	public AttachmentImpl(String name, File file, String mime)
	{
		this(name, file.getName(), file, mime);
	}

	public AttachmentImpl(String name, String fileName, File file, String mime)
	{
		this.name     = name;
		this.fileName = fileName;
		this.file     = file;
		this.mime     = mime;
	}

	@Override public String getName(){return name;}
	@Override public String getFileName(){return fileName;}
	@Override public String getMime() {return mime;}
	@Override public File getFile(){return file;}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof AttachmentImpl))
		{
			return false;
		}
		AttachmentImpl other = AttachmentImpl.class.cast(obj);
		
		return Objects.equals(name, other.name)
				&& Objects.equals(fileName, other.fileName)
				&& Objects.equals(file, other.file)
				&& Objects.equals(mime, other.mime);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(name) 
				+ Objects.hashCode(fileName)
				+ Objects.hashCode(file)
				+ Objects.hashCode(mime);
	}
}