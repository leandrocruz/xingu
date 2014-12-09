package xingu.type.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import xingu.type.ObjectPopulator;
import xingu.type.ObjectType;
import xingu.type.ObjectType.Type;
import xingu.type.Param;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.collection.FluidMap;

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
						Object converted = null;
						if(Date.class.isAssignableFrom(clazz) && param != null)
						{
							converted = toDate(clazz, param, value);
						}
						else
						{
							converted = convert(clazz, param, value);
						}

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

	private Object toDate(Class<?> clazz, Param param, String value)
		throws ParseException
	{
		String format = param.format();
		if(StringUtils.isNotEmpty(format))
		{
			try
			{
				DateFormat df = new SimpleDateFormat(format);
				return df.parse(value);
			}
			catch(Throwable t)
			{
				throw new NotImplementedYet("Can't parse date '"+value+"'. Format is '"+format+"'");
			}
		}
		return null;
	}

	protected abstract Object convert(Class<?> clazz, Param param, String value)
		throws Exception;
}