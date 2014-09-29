package xingu.codec.impl;

import java.util.Map;

import xingu.codec.Codec;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonCodec
	implements Codec
{
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public Object decode(String text)
		throws Exception
	{
		return decode(text, Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		Object value = mapper.readValue(text, clazz);
		return (T) value;
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		return mapper.writeValueAsString(object);
	}
}