package br.com.ibnetwork.xingu.utils.clone.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.clone.CloneException;
import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;
import br.com.ibnetwork.xingu.utils.clone.ReferenceHandler;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ArrayListFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.BooleanFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ByteFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.CharFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.DoubleFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.FloatFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.IntegerFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.LongFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ShortFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.StringFastCloner;

public class ClonerSupport
	implements Cloner
{
	protected Map<Class<?>, FastCloner<?>> fastClonerByTargetType = new HashMap<Class<?>, FastCloner<?>>();
	
	protected Map<Class<?>, ReferenceHandler> handlerByType = new HashMap<Class<?>, ReferenceHandler>();
	
	public ClonerSupport()
	{
		fastClonerByTargetType.put(Byte.class, new ByteFastCloner());
		fastClonerByTargetType.put(Boolean.class, new BooleanFastCloner());
		fastClonerByTargetType.put(Short.class, new ShortFastCloner());
		fastClonerByTargetType.put(Integer.class, new IntegerFastCloner());
		fastClonerByTargetType.put(Float.class, new FloatFastCloner());
		fastClonerByTargetType.put(Long.class, new LongFastCloner());
		fastClonerByTargetType.put(Double.class, new DoubleFastCloner());
		fastClonerByTargetType.put(Character.class, new CharFastCloner());
		fastClonerByTargetType.put(String.class, new StringFastCloner());
		fastClonerByTargetType.put(ArrayList.class, new ArrayListFastCloner());
	}
	
	@Override
	public <T> T deepClone(T original)
		throws CloneException
	{
		T clone = handle(original);
		if(clone != null)
		{
			return clone;
		}
		
		clone = tryFastClone(original);
		if(clone != null)
		{
			return clone;
		}
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) original.getClass();
		
		clone = newInstanceOf(clazz);
		List<Field> fields = FieldUtils.getAllFields(clazz);
		for (Field field : fields)
		{
			cloneField(field, original, clone);
		}
		
		return clone;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T handle(T original)
	{
		Class<T> clazz = (Class<T>) original.getClass();
		ReferenceHandler handler = handlerByType.get(clazz);
		if(handler == null)
		{
			return null;
		}
		return (T) handler.handle(original);
	}

	protected <T> void cloneField(Field field, T original, T clone)
	{
		int modifiers = field.getModifiers();
		boolean isTransient = Modifier.isTransient(modifiers);
		boolean isStatic = Modifier.isStatic(modifiers);
		if(isTransient || isStatic)
		{
			return;
		}
		
		//TODO: add Annotation handler
		//TODO: add Type handler

		boolean isPrimitive = field.getType().isPrimitive();
		boolean tryPrimitives = false;
		if(isPrimitive && tryPrimitives)
		{
			FieldUtils.copyPrimitive(field, original, clone);
		}
		else
		{
			Object value = FieldUtils.valueFrom(field, original);
			Object clonedValueForField = value == null ? null : deepClone(value);
			FieldUtils.set(field, clone, clonedValueForField);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T tryFastClone(T t)
	{
		Class<?> clazz = t.getClass();
		if(clazz.isArray())
		{
			return cloneArray(t);
		}

		FastCloner<T> fast = (FastCloner<T>) fastClonerByTargetType.get(clazz);
		if(fast == null)
		{
			return null;
		}
		
		return fast.clone(t, this);
	}

	@SuppressWarnings("unchecked")
	private <T> T cloneArray(T t)
	{
		Class<?> clazz = t.getClass();
		int length = Array.getLength(t);
		T newInstance = (T) Array.newInstance(clazz.getComponentType(), length);
		for (int i = 0; i < length; i++)
		{
		        Object item = Array.get(t, i);
		        Object clone = deepClone(item);
		        Array.set(newInstance, i, clone);
		}
		return newInstance;
	}
	
	protected <T> T newInstanceOf(Class<T> clazz)
		throws CloneException
	{
		try
		{
			return clazz.newInstance();
		}
		catch (Throwable t)
		{
			throw new CloneException("Error creating new instance of '" + clazz + "'", t);
		}
	}

	@Override
	public void addHandler(Class<?> clazz, ReferenceHandler handler)
	{
		handlerByType.put(clazz, handler);
	}
}
