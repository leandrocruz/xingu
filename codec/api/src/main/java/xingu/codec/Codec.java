package xingu.codec;

import java.util.List;

import xingu.lang.NotImplementedYet;

public interface Codec
{
	Object decode(String text)
		throws Exception;
	
	<T> T decode(String text, Class<? extends T> clazz)
		throws Exception;

	default <T> List<T> decodeList(String text, Class<? extends T> clazz)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	String encode(Object object)
		throws Exception;
}
