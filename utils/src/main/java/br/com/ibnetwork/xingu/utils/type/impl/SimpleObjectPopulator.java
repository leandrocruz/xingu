package br.com.ibnetwork.xingu.utils.type.impl;

import java.lang.reflect.Field;

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
	protected Object convert(Field field, Param param, String value)
		throws Exception
	{
		Class<?>    clazz   = field.getType();
		Type        type    = ObjectType.typeFor(clazz);
		TypeHandler handler = registry.handlerFor(clazz, type);
		return handler.toObject(value);
	}
}
