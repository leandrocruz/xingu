package xingu.clone.impl;

import java.lang.reflect.Field;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import org.apache.avalon.framework.activity.Initializable;

import xingu.clone.ObjectCloner;
import xingu.utils.FieldUtils;


public class ObjectClonerImpl
	implements ObjectCloner, Initializable
{
	private ClassPool pool;
	
	@Override
	public void initialize()
	{
		pool = ClassPool.getDefault();
	}

	@Override
	public Object build(Object source, String name)
		throws Exception
	{
		List<Field> fields = FieldUtils.getAllFields(source.getClass());

		CtClass cc = pool.getOrNull(name);
		if(cc == null)
		{
			cc = pool.makeClass(name);
			for (Field field : fields)
			{
				CtClass type = typeOf(field);
				CtField f = new CtField(type, field.getName(), cc);
				cc.addField(f);
			}
			
		}

		Class<?> clazz = cc.toClass();
		Object copy = clazz.newInstance();
		for (Field field : fields)
		{
			Field f2 = FieldUtils.getField(copy.getClass(), field.getName());
			Object value = FieldUtils.valueFrom(field, source);
			FieldUtils.set(f2, copy, value);
		}
		
		return copy;
	}

	private CtClass typeOf(Field field)
		throws NotFoundException
	{
		String name = field.getType().getName();
		CtClass result= pool.get(name);
		return result;
	}
}
