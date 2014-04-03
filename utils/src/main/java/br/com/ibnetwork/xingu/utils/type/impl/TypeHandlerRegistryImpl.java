package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;

public class TypeHandlerRegistryImpl
	implements TypeHandlerRegistry
{
	private static Map<String, TypeHandler> handlerByClass = new HashMap<String, TypeHandler>();
	private static Map<String, TypeHandler> handlerByName = new HashMap<String, TypeHandler>();
	
	static {
		_register(new BooleanTypeHandler());
		_register(new IntegerTypeHandler());
		_register(new StringTypeHandler());
		_register(new DateTypeHandler());
	}

	@Override
	public void register(TypeHandler handler)
	{
		_register(handler);
	}

	private static void _register(TypeHandler handler)
	{
		Class<?> clazz = handler.clazz();
		String name = handler.name();

		handlerByClass.put(clazz.getName(), handler);
		handlerByName.put(name, handler);
	}

	@Override
	public TypeHandler handlerFor(Class<?> clazz, Type type)
	{
		TypeHandler handler = get(clazz.getName());
		if(handler == null)
		{
			handler = getAlias(clazz, type);
			register(handler);
		}
		return handler;
	}

	private TypeHandler getAlias(Class<?> clazz, Type type)
	{
		String name = clazz.getName();
		switch(type)
		{
			case ARRAY:
				Class<?> componentType = clazz.getComponentType();
				name = handlerFor(componentType, null).name();
				break;

			default:
				break;
		}
		return new GenericTypeHandler(clazz, name, type);
	}

	@Override
	public TypeHandler get(String nameOrClass)
	{
		TypeHandler handler = handlerByName.get(nameOrClass);
		if(handler == null)
		{
			handler = handlerByClass.get(nameOrClass); 
		}
		return handler;
	}
}