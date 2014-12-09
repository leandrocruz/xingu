package xingu.type.impl;

import xingu.type.ObjectPopulator;
import xingu.type.ObjectType;
import xingu.type.ObjectType.Type;
import xingu.type.Param;
import xingu.type.TypeHandler;
import xingu.type.TypeHandlerRegistry;

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
