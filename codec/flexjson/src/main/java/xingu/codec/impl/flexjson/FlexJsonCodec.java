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
	private JSONDeserializer<Map<String, Object>> toObject;
	
	private JSONSerializer toJson;

	@Override
	public void initialize()
		throws Exception
	{
		toObject = new JSONDeserializer<Map<String, Object>>();
		toJson = new JSONSerializer();
		toJson.transform(new DateTransformer("dd/MM/yyyy HH:mm:ss Z"), Date.class); //used on Fortius ClientHandler
	}

	@Override
	public Object decode(String text)
	{
		Object obj = toObject.deserialize(text);
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
		String text = toJson.deepSerialize(object);
		return text;
	}
}
