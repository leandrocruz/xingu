package br.com.ibnetwork.xingu.utils.inspector;

import java.lang.reflect.Field;

public interface ObjectVisitor<T>
{
	void onNodeStart(Object obj, String id, TypeAlias alias, Field field);

	void onNodeEnd(Object obj, String id, TypeAlias alias, Field field);
	
	void onNodeReference(Object obj, String id, TypeAlias alias, Field field);
	
	void onPrimitiveObjectField(Field field, Object value);
	
	void onPrimitiveCollectionItem(Object value, String id, TypeAlias alias);

	T getResult();

	//void whenNodeEmpty(Field field);	
}
