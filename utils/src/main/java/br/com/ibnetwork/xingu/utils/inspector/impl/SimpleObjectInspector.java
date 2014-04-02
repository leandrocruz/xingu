package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;
import br.com.ibnetwork.xingu.utils.inspector.TypeAlias;
import br.com.ibnetwork.xingu.utils.inspector.TypeAliasMap;

public class SimpleObjectInspector
	implements ObjectInspector
{
	private Object       root;

	private TypeAliasMap aliases;

	private Map<String,  Object>  identityToObject = new HashMap<String, Object>();
	
	public SimpleObjectInspector(Object root, TypeAliasMap aliases)
	{
		this.root    = root;
		this.aliases = aliases;
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
			obj = FieldUtils.valueFrom(field, obj);
			if(obj == null)
			{
				return;
			}
			clazz = field.getType();
		}
		else
		{
			clazz = obj.getClass();
		}
		Type      type  = ObjectType.typeFor(clazz);
		TypeAlias alias = aliases.aliasFor(clazz, type);
		String    id    = Integer.toHexString(System.identityHashCode(obj));
		Object    ref   = identityToObject.get(id);
		if(ref != null)
		{
			visitor.onNodeReference(ref, id, alias, field);
			return;
		}
		
		identityToObject.put(id, obj);

		boolean isPrimitive = type == Type.PRIMITIVE || type == Type.NATIVE;
		if(isPrimitive && field != null)
		{
			int     modifiers   = field.getModifiers();
			boolean isTransient = Modifier.isTransient(modifiers);
			boolean isStatic    = Modifier.isStatic(modifiers);
			if(isTransient || isStatic)
			{
				return;
			}
		}
		
		if(!isPrimitive)
		{
			visitor.onNodeStart(obj, id, alias, field);
		}
		
		switch(type)
		{
			case ARRAY:
				visitArray(obj, visitor);
				break;
				
			case COLLECTION:
			case MAP:
				visitCollection(obj, visitor);
				break;
				
			case OBJECT:
				visitObject(obj, visitor);
				break;
				
			case PRIMITIVE:
			case NATIVE:
				visitor.onPrimitive(obj, id, alias, field);
				break;
			
			default:
				throw new NotImplementedYet();
		}
		
		if(!isPrimitive)
		{
			visitor.onNodeEnd(obj, id, alias, field);
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
