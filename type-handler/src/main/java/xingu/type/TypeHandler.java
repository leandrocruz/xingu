package xingu.type;

import xingu.type.ObjectType.Type;


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