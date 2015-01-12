package xingu.codec.impl.xoia;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.codec.Codec;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.inspector.ObjectEmitter;
import xingu.inspector.ObjectVisitor;
import xingu.inspector.impl.SimpleObjectInspector;
import xingu.inspector.impl.XmlEmitter;
import xingu.inspector.impl.XmlReader;
import xingu.type.ObjectType;
import xingu.type.ObjectType.Type;
import xingu.type.TypeHandler;
import xingu.type.TypeHandlerRegistry;
import xingu.type.impl.TypeHandlerRegistryImpl;
import xingu.utils.ObjectUtils;
import xingu.utils.classloader.ClassLoaderManager;

public class XoiaCodec
	implements Codec, Configurable
{
	@Inject
	private ClassLoaderManager clm;
	
	@Inject
	private Factory factory;

	private TypeHandlerRegistry registry = new TypeHandlerRegistryImpl();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		Configuration[] handlers = conf.getChild("handlers").getChildren("handler");
		for(Configuration handlerConf : handlers)
		{
			TypeHandler handler = toTypeHandler(handlerConf);
			registry.register(handler);
		}
	}

	private TypeHandler toTypeHandler(Configuration conf)
		throws ConfigurationException
	{
		String   className = conf.getAttribute("class");
		String   forName   = conf.getAttribute("for");
		String   name      = conf.getAttribute("name");
		String   typeName  = conf.getAttribute("type", null);
		Class<?> forClass  = ObjectUtils.loadClass(forName);
		Type     type      = typeName == null ? ObjectType.Type.OBJECT : ObjectType.Type.valueOf(typeName);
		
		@SuppressWarnings("unchecked")
		Class<? extends TypeHandler> clazz = (Class<? extends TypeHandler>) ObjectUtils.loadClass(className);
		
		return factory.create(clazz, forClass, name, type);
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		ObjectVisitor<String> visitor = new XmlEmitter();
		new SimpleObjectInspector(object, registry).visit(visitor);
		String result = visitor.getResult();
		//System.err.println("ENC >> " + result);
		return result;
	}

	@Override
	public Object decode(String text)
		throws Exception
	{
		//System.err.println("DEC << " + text);
		ObjectEmitter deserializer = new XmlReader(registry, clm);
		return deserializer.from(text);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		return (T) decode(text);
	}
}
