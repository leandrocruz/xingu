package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class TypeHandlerRegistryImpl
	implements TypeHandlerRegistry
{
	private static Map<String, TypeHandler> handlerByClass = new HashMap<String, TypeHandler>();
	private static Map<String, TypeHandler> handlerByName = new HashMap<String, TypeHandler>();
	
	static {
		_register(new ByteTypeHandler());
		_register(new BooleanTypeHandler());
		_register(new CharTypeHandler());
		_register(new IntegerTypeHandler());
		_register(new LongTypeHandler());
		_register(new StringTypeHandler());
		_register(new FloatTypeHandler());
		_register(new DoubleTypeHandler());
		_register(new DateTypeHandler());
		_register(new EmptySetTypeHandler());
		_register(new EmptyMapTypeHandler());
		_register(new EmptyListTypeHandler());
		_register(new EnumTypeHandler());
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
		String      name    = toName(clazz, type);
		TypeHandler handler = get(name);
		if(handler == null)
		{
			handler = getAlias(clazz, type);
			register(handler);
		}
		return handler;
	}

	private String toName(Class<?> clazz, Type type)
	{
		switch(type)
		{
			case ENUM:
				return "enum"; //must match EnumTypeHandler.name
						
			case ARRAY:
				return clazz.getComponentType().getName() + "[]";
			
			case MAP:
				return clazz.getName(); //clazz.getComponentType().getName() + "{}";
				
			default:
				return clazz.getName();
		}
	}

	private TypeHandler getAlias(Class<?> clazz, Type type)
	{
		String name = clazz.getName();
		switch(type)
		{
			case ARRAY:
				Class<?> componentType = clazz.getComponentType();
				name = handlerFor(componentType, Type.OBJECT).name() + "[]";
				return new ArrayTypeHandler(clazz, name);

			default:
				return new GenericTypeHandler(clazz, name, type);
		}
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
