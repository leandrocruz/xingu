package br.com.ibnetwork.xingu.utils.inspector;

import java.lang.reflect.Field;

public interface ObjectVisitor<T>
{
	void onNodeStart(Object obj, String id, TypeHandler handler, Field field);

	void onNodeEnd(Object obj, String id, TypeHandler handler, Field field);
	
	void onNodeReference(Object obj, String id, TypeHandler handler, Field field);

	void onPrimitive(Object obj, String id, TypeHandler handler, Field field);

	T getResult();
}
