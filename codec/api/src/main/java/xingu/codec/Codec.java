package xingu.codec;



public interface Codec
{
	Object decode(String text)
		throws Exception;
	
	<T> T decode(String text, Class<? extends T> clazz)
		throws Exception;

	String encode(Object object)
		throws Exception;
}
