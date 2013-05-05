package xingu.node.commons.protocol;


public interface ProtocolCodec
{
	String decode(long keyId, byte[] bytes, int type)
		throws Exception;

	byte[] encode(long keyId, String input, int type)
			throws Exception;

	int typeFrom(Object obj);

}
