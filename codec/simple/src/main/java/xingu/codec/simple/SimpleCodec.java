package xingu.codec.simple;

import java.io.StringWriter;

import org.apache.avalon.framework.activity.Initializable;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.codec.Codec;
import xingu.codec.impl.CodecSupport;

public class SimpleCodec
	extends CodecSupport
	implements Codec, Initializable
{
	private Serializer	serializer;

	@Override
	public void initialize()
		throws Exception
	{
		serializer = new Persister();
	}

	@Override
	public Object decode(String text)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		return serializer.read(clazz, text);
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		StringWriter writer = new StringWriter();
		serializer.write(object, writer);
		return writer.toString();
	}
}