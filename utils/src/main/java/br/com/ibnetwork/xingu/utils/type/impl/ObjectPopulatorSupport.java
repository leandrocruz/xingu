package br.com.ibnetwork.xingu.utils.type.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.type.ObjectPopulator;
import br.com.ibnetwork.xingu.utils.type.Param;

public abstract class ObjectPopulatorSupport
	implements ObjectPopulator
{
	@Override
	public void populate(Object obj, Map<String, String> map)
		throws Exception
	{
		List<Field> allFields = FieldUtils.getAllFields(obj.getClass());
		for(Field field : allFields)
		{
			String      fieldName = field.getName();
			Param       param     = field.getAnnotation(Param.class);
			String      value     = param != null ? map.get(param.name()) : map.get(fieldName);
			if(value != null)
			{
				Object converted = convert(field, param, value);
				if(converted != null)
				{
					FieldUtils.set(field, obj, converted);
				}
			}
		}
	}

	protected abstract Object convert(Field field, Param param, String value)
		throws Exception;
}