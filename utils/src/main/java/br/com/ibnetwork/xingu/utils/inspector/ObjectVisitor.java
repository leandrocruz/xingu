package br.com.ibnetwork.xingu.utils.inspector;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.type.TypeHandler;

public interface ObjectVisitor<T>
{
	void onNodeStart(Object obj, String id, TypeHandler handler, Field field);

	void onNodeEnd(Object obj, String id, TypeHandler handler, Field field);
	
	void onNodeReference(Object obj, String id, TypeHandler handler, Field field);

	void onPrimitive(Object obj, String id, TypeHandler handler, Field field);

	T getResult();
}
