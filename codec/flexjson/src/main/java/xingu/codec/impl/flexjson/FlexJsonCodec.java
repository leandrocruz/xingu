package xingu.codec.impl.flexjson;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.codec.Codec;
import xingu.codec.impl.CodecSupport;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.Constructors;
import br.com.ibnetwork.xingu.utils.ObjectUtils;
import br.com.ibnetwork.xingu.utils.StringUtils;
import flexjson.ClassFinder;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectFactory;
import flexjson.transformer.Transformer;

public class FlexJsonCodec
	extends CodecSupport
	implements Codec, Configurable, Initializable
{
	@Inject
	private Factory factory;
	
	private JSONDeserializer<Map<String, Object>> decoder;
	
	private JSONSerializer encoder;

	private boolean prettyPrint;
	
	private Map<Class<?>, Transformer> transformerByClass = new HashMap<Class<?>, Transformer>();
	
	private Map<Class<?>, ObjectFactory> factoryByClass = new HashMap<Class<?>, ObjectFactory>();
	
	
	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration enc = conf.getChild("encoder");
		prettyPrint = enc.getAttributeAsBoolean("prettyPrint", true); 
		
		try
		{
			addTransformers(conf);
			addObjectFactories(conf);
		}
		catch(Exception e)
		{
			throw new ConfigurationException("", e);
		}
	}

	private void addTransformers(Configuration conf)
		throws Exception
	{
		Configuration[] transformers = conf.getChild("transformers").getChildren("transformer");
		for (Configuration c : transformers)
		{
			Class<?> clazz = ObjectUtils.loadClass(c.getAttribute("class"));
			String arg = c.getAttribute("arg", null);
			Object instance = null;
			if(StringUtils.isEmpty(arg))
			{
				instance = ObjectUtils.getInstance(clazz);
			}
			else
			{
				Constructor<?> constructor = Constructors.findConstructor(clazz, arg);
				constructor.setAccessible(true);
				instance = constructor.newInstance(arg);
			}

	        Class<?> forClass = ObjectUtils.loadClass(c.getAttribute("for"));
			transformerByClass.put(forClass, (Transformer) instance);
		}
	}

	private void addObjectFactories(Configuration conf)
		throws Exception
	{
		Configuration[] factories = conf.getChild("factories").getChildren("factory");
		for (Configuration c : factories)
		{
			Class<?> clazz = ObjectUtils.loadClass(c.getAttribute("class"));
			Object instance = ObjectUtils.getInstance(clazz);
			Class<?> forClass = ObjectUtils.loadClass(c.getAttribute("for"));
			factoryByClass.put(forClass, (ObjectFactory) instance);
		}
	}

	@Override
	public void initialize()
		throws Exception
	{
		ClassFinder finder = factory.create(XinguClassFinder.class);
		decoder = new JSONDeserializer<Map<String, Object>>(finder);
		for(Class<?> clazz : factoryByClass.keySet())
		{
			decoder.use(clazz, factoryByClass.get(clazz));
		}
		
		encoder = new JSONSerializer();
		encoder.prettyPrint(prettyPrint);
		for(Class<?> clazz : transformerByClass.keySet())
		{
			encoder.transform(transformerByClass.get(clazz), clazz);
		}
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
