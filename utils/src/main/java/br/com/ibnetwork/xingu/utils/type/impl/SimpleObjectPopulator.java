package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.type.ObjectPopulator;
import br.com.ibnetwork.xingu.utils.type.ObjectType;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.Param;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;

public class SimpleObjectPopulator
	extends ObjectPopulatorSupport
{
	private static ObjectPopulator INSTANCE = new SimpleObjectPopulator(new TypeHandlerRegistryImpl());
	
	private TypeHandlerRegistry	registry;

	public SimpleObjectPopulator(TypeHandlerRegistry registry)
	{
		this.registry = registry;
	}

	public static ObjectPopulator instance()
	{
		return INSTANCE;
	}

	@Override
	protected Object convert(Class<?> clazz, Param param, String value)
		throws Exception
	{
		Type        type    = ObjectType.typeFor(clazz);
		TypeHandler handler = registry.handlerFor(clazz, type);
		return handler.toObject(value);
	}
}
