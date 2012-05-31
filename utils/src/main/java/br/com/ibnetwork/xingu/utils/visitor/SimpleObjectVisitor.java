package br.com.ibnetwork.xingu.utils.visitor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.FieldUtils;

public class SimpleObjectVisitor
	implements ObjectVisitor
{
	private Map<Class<?>, Boolean> skipObjectByClass = new HashMap<Class<?>, Boolean>();
	
	public SimpleObjectVisitor()
	{
		skipObjectByClass.put(Object.class, true);
		skipObjectByClass.put(Byte.class, true);
		skipObjectByClass.put(Character.class, true);
		skipObjectByClass.put(String.class, true);
		skipObjectByClass.put(Short.class, true);
		skipObjectByClass.put(Integer.class, true);
		skipObjectByClass.put(Long.class, true);
		skipObjectByClass.put(Float.class, true);
		skipObjectByClass.put(Double.class, true);
	}
	
	@Override
	public void visit(Object obj)
	{
		if(obj == null)
		{
			return;
		}
		Class<?> clazz = obj.getClass();
		Boolean skip = skipObjectByClass.get(clazz);
		if(skip != null && skip)
		{
			return;
		}
		
		if(clazz.isArray())
		{
			visitArray(obj);
		}
		else if(obj instanceof Collection)
		{
			visitCollection(obj);
		}
		else
		{
			visitObject(obj);
		}
	}

	private void visitCollection(Object obj)
	{
		Collection<?> coll = (Collection<?>) obj;
		Iterator<?> it = coll.iterator();
		while (it.hasNext())
		{
			Object item = it.next();
			visit(item);
		}
	}

	private void visitArray(Object obj)
	{
		int length = Array.getLength(obj);
		for (int i = 0; i < length; i++)
		{
			Object item = Array.get(obj, i);
			visit(item);
		}
	}

	private void visitObject(Object obj)
	{
		Class<?> clazz = obj.getClass();
		List<Field> fields = FieldUtils.getAllFields(clazz);
		for (Field field : fields)
		{
			int modifiers = field.getModifiers();
			boolean isTransient = Modifier.isTransient(modifiers);
			boolean isStatic = Modifier.isStatic(modifiers);
			if(isTransient || isStatic)
			{
				return;
			}

			Object value = FieldUtils.valueFrom(field, obj);
			visit(obj, field, value);
			boolean isPrimitive = field.getType().isPrimitive();
			if(!isPrimitive && value != null)
			{
				visit(value);
			}
		}
	}

	protected void visit(Object obj, Field field, Object value)
	{
		//System.out.println(field.getName() + " = " + value);
	}

	public void replace(Object obj, Field field, Object value)
	{
		FieldUtils.set(field, obj, value);
	}
}
