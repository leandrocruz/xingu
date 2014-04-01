package br.com.ibnetwork.xingu.utils.inspector;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public interface ObjectVisitor
{
	void nodeStart(Object obj, Field field, Type type);
	void nodeEnd(Object obj, Field field, Type type);
	void nodeEmpty(Field field);

	void field(Object obj, Field field, Object value);
}
