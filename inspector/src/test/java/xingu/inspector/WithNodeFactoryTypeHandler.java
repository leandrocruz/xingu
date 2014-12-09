package xingu.inspector;

import xingu.type.ObjectType.Type;
import xingu.type.impl.TypeHandlerSupport;

public class WithNodeFactoryTypeHandler
	extends TypeHandlerSupport
	implements NodeFactory
{
	String name;
	
	int value;
	
	public WithNodeFactoryTypeHandler()
	{
		super(WithNodeFactory.class, "test", Type.OBJECT);
	}

	@Override
	public Object newInstance(ClassLoader cl)
		throws Exception
	{
		return this;
	}

	@Override
	public Object create()
	{
		return new WithNodeFactory(name, value);
	}
}