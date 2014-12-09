package xingu.type.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import xingu.type.ObjectType.Type;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.io.FileUtils;


public class FileTypeHandler
	extends TypeHandlerSupport
{
	public FileTypeHandler()
	{
		super(File.class, "file", Type.PRIMITIVE);
	}

	@Override
	public String toString(Object obj)
	{
		File file = File.class.cast(obj);
		if(!file.exists())
		{
			return null;
		}
		
		if(file.isDirectory())
		{
			//TODO:
			return "ERROR: file is directory";
		}
		
		try
		{
			byte[] bytes = FileUtils.toByteArray(file);
			byte[] encoded = Base64.encodeBase64(bytes);
			return new String(encoded);
		}
		catch(IOException e)
		{
			String trace = ExceptionUtils.getStackTrace(e);
			return "ERROR: " + e.getMessage() + "\n" + trace;
		}
	}

	@Override
	public Object toObject(String value)
	{
		if(StringUtils.isEmpty(value))
		{
			return null;
		}
		
		if(value.startsWith("ERROR"))
		{
			return null;
		}

		try
		{
			byte[] bytes = Base64.decodeBase64(value.getBytes());
			File file = File.createTempFile("fileTypeHandler-", ".tmp");
			FileUtils.toFile(bytes, file);
			return file;
		}
		catch(Throwable t)
		{
			return null;
		}
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}