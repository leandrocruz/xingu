package xingu.type;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectType
{
	public static enum Type {
		PRIMITIVE,
		ENUM,
		OBJECT, 
		ARRAY, 
		COLLECTION,
		MAP
	};

	public static final Map<Class<?>, Type>	typeByClass = new HashMap<Class<?>, Type>();
	
	static {
		typeByClass.put(Object.class, 		Type.PRIMITIVE);
		typeByClass.put(String.class, 		Type.PRIMITIVE);
		typeByClass.put(Boolean.class, 		Type.PRIMITIVE);
		typeByClass.put(Byte.class, 		Type.PRIMITIVE);
		typeByClass.put(Character.class, 	Type.PRIMITIVE);
		typeByClass.put(Short.class, 		Type.PRIMITIVE);
		typeByClass.put(Integer.class, 		Type.PRIMITIVE);
		typeByClass.put(Long.class, 		Type.PRIMITIVE);
		typeByClass.put(Float.class, 		Type.PRIMITIVE);
		typeByClass.put(Double.class, 		Type.PRIMITIVE);
		typeByClass.put(Date.class, 		Type.PRIMITIVE);
		typeByClass.put(File.class, 		Type.PRIMITIVE);
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

		if(clazz.isEnum())
		{
			return Type.ENUM;
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
