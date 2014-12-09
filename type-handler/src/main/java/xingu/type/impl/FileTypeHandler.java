package xingu.type.impl;

import java.io.File;

import org.apache.commons.codec.binary.Base64;

import xingu.type.ObjectType.Type;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;


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
		byte[] bytes = null;
		byte[] encoded = Base64.encodeBase64(bytes);
		return new String(encoded);
	}

	@Override
	public Object toObject(String value)
	{
		byte[] bytes = Base64.decodeBase64(value.getBytes());
		throw new NotImplementedYet();
	}
}
