package br.com.ibnetwork.xingu.utils.type;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;


public interface TypeHandler
{
	Type type();

	String name();
	
	Class<?> clazz();

	String toString(Object obj);

	Object toObject(String value);

	Object newInstance(ClassLoader cl)
		throws Exception;
}