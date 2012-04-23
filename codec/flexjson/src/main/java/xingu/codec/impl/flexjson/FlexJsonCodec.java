package xingu.codec.impl.flexjson;

import java.util.Date;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import xingu.codec.Codec;

public class FlexJsonCodec
	implements Codec, Initializable
{
	private JSONDeserializer<Map<String, Object>> decoder;
	
	private JSONSerializer encoder;

	@Override
	public void initialize()
		throws Exception
	{
		decoder = new JSONDeserializer<Map<String, Object>>();
		encoder = new JSONSerializer();
		encoder.transform(new DateTransformer("dd/MM/yyyy HH:mm:ss Z"), Date.class); //used on Fortius ClientHandler
		encoder.prettyPrint(true);
	}

	@Override
	public Object decode(String text)
	{
		Object obj = decoder.deserialize(text);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T decode(String text, Class<? extends T> clazz)
	{
		return (T) decode(text);
	}

	@Override
	public String encode(Object object)
	{
		String text = encoder.deepSerialize(object);
		return text;
	}
}
