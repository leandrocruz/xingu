package xingu.utils.reflect;

import java.lang.reflect.Field;


public interface FieldHandler
{
	boolean isPrimitive();

	void transferValue(Field field, Object src, Object dst)
		throws IllegalArgumentException, IllegalAccessException;

	String typeName();
	
}
