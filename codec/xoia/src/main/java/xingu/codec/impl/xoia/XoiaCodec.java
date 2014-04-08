package xingu.codec.impl.xoia;

import xingu.codec.Codec;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.inspector.ObjectEmitter;
import br.com.ibnetwork.xingu.utils.inspector.impl.SimpleObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlEmitter;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlReader;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;
import br.com.ibnetwork.xingu.utils.type.impl.TypeHandlerRegistryImpl;

public class XoiaCodec
	implements Codec
{
	@Inject
	private ClassLoaderManager clm;

	private TypeHandlerRegistry registry = new TypeHandlerRegistryImpl();
	
	@Override
	public String encode(Object object)
		throws Exception
	{
		XmlEmitter visitor = new XmlEmitter();
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
