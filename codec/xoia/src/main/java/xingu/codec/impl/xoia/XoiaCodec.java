package xingu.codec.impl.xoia;

import xingu.codec.Codec;
import br.com.ibnetwork.xingu.utils.inspector.ObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.TypeAliasMap;
import br.com.ibnetwork.xingu.utils.inspector.impl.SimpleObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.impl.TypeAliasMapImpl;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlEmitter;

public class XoiaCodec
	implements Codec
{
	private TypeAliasMap aliases = new TypeAliasMapImpl();
	
	@Override
	public Object decode(String text)
		throws Exception
	{
		return null;
	}

	@Override
	public Object decode(String text, ClassLoader cl)
		throws Exception
	{
		return null;
	}

	@Override
	public <T> T decode(String text, Class<? extends T> clazz)
		throws Exception
	{
		return null;
	}

	@Override
	public String encode(Object object)
		throws Exception
	{
		XmlEmitter      visitor   = new XmlEmitter();
		ObjectInspector inspector = new SimpleObjectInspector(object, aliases);
		inspector.visit(visitor);
		return visitor.getResult();
	}
}
