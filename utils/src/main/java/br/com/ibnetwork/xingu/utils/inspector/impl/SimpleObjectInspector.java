package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType;
import static br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;

public class SimpleObjectInspector
	implements ObjectInspector
{
	private Object	root;

	public SimpleObjectInspector(Object root)
	{
		this.root = root;
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
			visitor.nodeEmpty(field);
			return;
		}

		Class<?> clazz = obj.getClass();
		Type     type  = ObjectType.typeFor(clazz);
		visitor.nodeStart(obj, field, type);
		switch(type)
		{
			case ARRAY:
				visitArray(obj, field, visitor);
				break;
				
			case COLLECTION:
			case MAP:
				visitCollection(obj, field, visitor);
				break;

			case PRIMITIVE:
			case NATIVE:
				throw new NotImplementedYet();
				
			case OBJECT:
				visitObject(obj, field, visitor);
				break;
				
			default:
				throw new NotImplementedYet();
		}
		visitor.nodeEnd(obj, field, type);
	}

	private void visitCollection(Object obj, Field field, ObjectVisitor<?> visitor)
	{
		Collection<?> coll = (Collection<?>) obj;
		Iterator<?> it = coll.iterator();
		while (it.hasNext())
		{
			Object item = it.next();
			visitNode(item, field, visitor);
		}
	}

	private void visitArray(Object obj, Field field, ObjectVisitor<?> visitor)
	{
		int length = Array.getLength(obj);
		for (int i = 0; i < length; i++)
		{
			Object item = Array.get(obj, i);
			visitNode(item, field, visitor);
		}
	}

	private void visitObject(Object obj, Field notUsed, ObjectVisitor<?> visitor)
	{
		Class<?>    clazz  = obj.getClass();
		List<Field> fields = FieldUtils.getAllFields(clazz);
		for (Field field : fields)
		{
			int     modifiers   = field.getModifiers();
			boolean isTransient = Modifier.isTransient(modifiers);
			boolean isStatic    = Modifier.isStatic(modifiers);
			if(isTransient || isStatic)
			{
				return;
			}

			Class<?> fieldClass = field.getType();
			Type     type       = ObjectType.typeFor(fieldClass);
			Object   value      = FieldUtils.valueFrom(field, obj);
			
			switch(type)
			{
				case PRIMITIVE:
				case NATIVE:
					visitor.field(obj, field, value);
					break;

				case ARRAY:
				case COLLECTION:
				case MAP:
				case OBJECT:
					visitNode(value, field, visitor);
					break;
					
				default:
					throw new NotImplementedYet();
			}
		}
	}
}
