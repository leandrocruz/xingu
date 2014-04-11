package br.com.ibnetwork.xingu.utils.type.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.type.ObjectPopulator;
import br.com.ibnetwork.xingu.utils.type.ObjectType;
import br.com.ibnetwork.xingu.utils.type.Param;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;

public class SimpleObjectPopulator
	implements ObjectPopulator
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
	public void populate(Object obj, Map<String, String> map)
		throws Exception
	{
		List<Field> allFields   = FieldUtils.getAllFields(obj.getClass());
		for(Field field : allFields)
		{
			String      fieldName = field.getName();
			Param       param     = field.getAnnotation(Param.class);
			String      value     = param != null ? map.get(param.name()) : map.get(fieldName);
			if(value != null)
			{
				Class<?>    type      = field.getType();
				TypeHandler handler   = registry.handlerFor(type, ObjectType.typeFor(type));
				Object      converted = handler.toObject(value);
				if(value != null)
				{
					FieldUtils.set(field, obj, converted);
				}
			}
		}
	}
}
