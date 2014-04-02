package br.com.ibnetwork.xingu.utils.inspector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ObjectType
{
	public static enum Type {
		PRIMITIVE,
		NATIVE,
		OBJECT, 
		ARRAY, 
		COLLECTION,
		MAP
	};

	public static final Map<Class<?>, Type>	typeByClass = new HashMap<Class<?>, Type>();
	
	static {
		typeByClass.put(Object.class, 		Type.NATIVE);
		typeByClass.put(String.class, 		Type.NATIVE);
		typeByClass.put(Boolean.class, 		Type.PRIMITIVE);
		typeByClass.put(Byte.class, 		Type.PRIMITIVE);
		typeByClass.put(Character.class, 	Type.PRIMITIVE);
		typeByClass.put(Short.class, 		Type.PRIMITIVE);
		typeByClass.put(Integer.class, 		Type.PRIMITIVE);
		typeByClass.put(Long.class, 		Type.PRIMITIVE);
		typeByClass.put(Float.class, 		Type.PRIMITIVE);
		typeByClass.put(Double.class, 		Type.PRIMITIVE);
	}

	public static Type typeFor(Class<?> clazz)
	{
		Type     result = typeByClass.get(clazz);
		if(result != null)
		{
			return result;
		}
		
		if(clazz.isPrimitive())
		{
			return Type.PRIMITIVE;
		}
		
		if(clazz.isArray())
		{
			return Type.ARRAY;
		}

		if(Map.class.isAssignableFrom(clazz))
		{
			return Type.MAP;
		}

		if(Collection.class.isAssignableFrom(clazz))
		{
			return Type.COLLECTION;
		}
		
		return Type.OBJECT;
	}
}
