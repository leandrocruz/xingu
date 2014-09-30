package br.com.ibnetwork.xingu.utils.type.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.collection.FluidMap;
import br.com.ibnetwork.xingu.utils.type.ObjectPopulator;
import br.com.ibnetwork.xingu.utils.type.ObjectType;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.Param;

public abstract class ObjectPopulatorSupport
	implements ObjectPopulator
{

	@Override
	public void populate(Object obj, FluidMap<String> map)
		throws Exception
	{
		List<Field> allFields = FieldUtils.getAllFields(obj.getClass());
		for(Field field : allFields)
		{
			String  fieldName = field.getName();
			Param   param     = field.getAnnotation(Param.class);
			String  name      = param != null ? param.name() : fieldName;
			boolean has       = map.hasValueFor(name);
			if(has)
			{
				Class<?> clazz = field.getType();
				Type     type  = ObjectType.typeFor(clazz);
				switch(type)
				{
					case OBJECT:
					case PRIMITIVE:
						String value     = map.get(name);
						Object converted = convert(clazz, param, value);
						if(converted != null)
						{
							FieldUtils.set(field, obj, converted);
						}
						break;
					
					case ARRAY:
						
						Class<?>     actual = clazz.getComponentType();
						List<String> values = map.getAll(name);
						int          size   = values.size();
						Object       array  = Array.newInstance(actual, size);
						FieldUtils.set(field, obj, array);

						for(int i = 0; i < size; i++)
						{
							value     = values.get(i);
							converted = convert(actual, param, value);
							if(converted != null)
							{
								Array.set(array, i, converted);
							}
						}

						break;

					default:
						throw new NotImplementedYet();
				}
			}
		}
	}

	protected abstract Object convert(Class<?> clazz, Param param, String value)
		throws Exception;
}