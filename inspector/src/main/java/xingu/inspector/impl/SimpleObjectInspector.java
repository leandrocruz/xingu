package xingu.inspector.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xingu.inspector.Ignore;
import xingu.inspector.ObjectInspector;
import xingu.inspector.ObjectVisitor;
import xingu.lang.NotImplementedYet;
import xingu.type.ObjectType;
import xingu.type.ObjectType.Type;
import xingu.type.TypeHandler;
import xingu.type.TypeHandlerRegistry;
import xingu.utils.FieldUtils;

public class SimpleObjectInspector
	implements ObjectInspector
{
	private Object				root;

	private TypeHandlerRegistry	registry;

	private Map<Object, String>	identityByObject	= new IdentityHashMap<Object, String>();

	private int					nextId				= 1;
	
	public SimpleObjectInspector(Object root, TypeHandlerRegistry registry)
	{
		this.root     = root;
		this.registry = registry;
	}

	@Override
	public void visit(ObjectVisitor<?> visitor)
	{
		visitNode(root, null, visitor);
	}

	private void visitNode(Object obj, Field field, ObjectVisitor<?> visitor)
	{
		if(obj == null)
		{
			return;
		}

		Class<?> clazz = null;
		if(field != null)
		{
			int     modifiers   = field.getModifiers();
			boolean isTransient = Modifier.isTransient(modifiers);
			boolean isStatic    = Modifier.isStatic(modifiers);
			Ignore  ann         = field.getAnnotation(Ignore.class);
			boolean ignore      = ann == null ? false : ann.ignore();
			if(isTransient || isStatic || ignore)
			{
				return;
			}

			obj = FieldUtils.valueFrom(field, obj);
			if(obj == null)
			{
				return;
			}
		}
		clazz = obj.getClass();

		Type        type    = ObjectType.typeFor(clazz);
		TypeHandler handler = registry.handlerFor(clazz, type);
		String      id      = identityByObject.get(obj);
		if(id == null)
		{
			id = String.valueOf(nextId++);
			identityByObject.put(obj, id);
		}
		else
		{
			visitor.onNodeReference(obj, id, handler, field);
			return;
		}
		

		boolean isPrimitive = (type == Type.PRIMITIVE || type == Type.ENUM);
		if(!isPrimitive)
		{
			visitor.onNodeStart(obj, id, handler, field);
		}
		
		switch(type)
		{
			case ARRAY:
				visitArray(obj, visitor);
				break;
				
			case COLLECTION:
				visitCollection(obj, visitor);
				break;

			case MAP:
				visitMap(obj, visitor);
				break;
				
			case OBJECT:
				visitObject(obj, visitor);
				break;
				
			case ENUM:
			case PRIMITIVE:
				visitor.onPrimitive(obj, id, handler, field);
				break;
			
			default:
				throw new NotImplementedYet();
		}
		
		if(!isPrimitive)
		{
			visitor.onNodeEnd(obj, id, handler, field);
		}
	}

	private void visitMap(Object obj, ObjectVisitor<?> visitor)
	{
		Map<?,      ?>   map = (Map<?, ?>) obj;
		Set<?>      keys     = map.keySet();
		Iterator<?> it       = keys.iterator();
		while(it.hasNext())
		{
			Object key   = it.next();
			visitNode(key, null, visitor);

			Object value = map.get(key);
			if(value == null)
			{
				throw new NotImplementedYet();
			}
			visitNode(value, null, visitor);
		}
	}

	private void visitCollection(Object obj, ObjectVisitor<?> visitor)
	{
		Collection<?> coll = (Collection<?>) obj;
		Iterator<?> it = coll.iterator();
		while (it.hasNext())
		{
			Object item = it.next();
			visitNode(item, null, visitor);
		}
	}

	private void visitArray(Object obj, ObjectVisitor<?> visitor)
	{
		int length = Array.getLength(obj);
		for (int i = 0; i < length; i++)
		{
			Object item = Array.get(obj, i);
			visitNode(item, null, visitor);
		}
	}

	private void visitObject(Object obj, ObjectVisitor<?> visitor)
	{
		Class<?>    clazz  = obj.getClass();
		List<Field> fields = FieldUtils.getAllFields(clazz);
		for (Field field : fields)
		{
			visitNode(obj, field, visitor);
		}
	}
}
