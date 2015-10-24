package codec.impl.boon;

import org.apache.avalon.framework.activity.Initializable;
import org.boon.json.JsonParserFactory;
import org.boon.json.JsonSerializerFactory;
import org.boon.json.ObjectMapper;
import org.boon.json.implementation.ObjectMapperImpl;

import xingu.codec.Codec;
import xingu.lang.NotImplementedYet;

public class BoonJsonCodec
	implements Codec, Initializable
{
	private ObjectMapper mapper;

	@Override
	public void initialize()
		throws Exception
	{
		JsonSerializerFactory serializerFactory = new JsonSerializerFactory();
		JsonParserFactory     parserFactory     = new JsonParserFactory().lax();
		mapper = new ObjectMapperImpl(parserFactory, serializerFactory);
	}

	@Override
	public Object decode(String text)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		return mapper.writeValueAsString(object);
	}

	@Override
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		return mapper.readValue(text, clazz);
	}

}
