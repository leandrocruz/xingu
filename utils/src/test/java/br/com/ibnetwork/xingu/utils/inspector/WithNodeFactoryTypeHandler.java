package br.com.ibnetwork.xingu.utils.inspector;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.impl.TypeHandlerSupport;

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
	public Object newInstance()
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