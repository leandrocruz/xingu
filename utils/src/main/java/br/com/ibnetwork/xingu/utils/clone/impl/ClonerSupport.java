package br.com.ibnetwork.xingu.utils.clone.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import br.com.ibnetwork.xingu.lang.SilentObjectCreator;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.clone.CloneException;
import br.com.ibnetwork.xingu.utils.clone.Cloner;
import br.com.ibnetwork.xingu.utils.clone.CloningContext;
import br.com.ibnetwork.xingu.utils.clone.FastCloner;
import br.com.ibnetwork.xingu.utils.clone.ReferenceHandler;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ArrayListFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.BooleanFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ByteFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.CharFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.DoubleFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.FloatFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.HashMapFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.HashSetFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.IntegerFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.LongFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ObjectFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.ShortFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.StringFastCloner;
import br.com.ibnetwork.xingu.utils.clone.impl.fast.TreeSetFastCloner;

public class ClonerSupport
	implements Cloner
{
	protected Map<Class<?>, FastCloner<?>> fastClonerByTargetType = new HashMap<Class<?>, FastCloner<?>>();
	
	protected Map<Class<?>, ReferenceHandler> handlerByType = new HashMap<Class<?>, ReferenceHandler>();
	
	protected boolean pretty = false;
	
	public ClonerSupport()
	{
		this(false);
	}
	
	public ClonerSupport(boolean pretty)
	{
		this.pretty = pretty;
		fastClonerByTargetType.put(Object.class, new ObjectFastCloner());
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
		fastClonerByTargetType.put(HashMap.class, new HashMapFastCloner());
		fastClonerByTargetType.put(TreeSet.class, new TreeSetFastCloner());
		fastClonerByTargetType.put(HashSet.class, new HashSetFastCloner());
	}

	@Override
	public <T> T deepClone(T original)
		throws CloneException
	{
		return deepCloneWithContext(new CloningContext(), original);
	}
	
	@Override
	public <T> T deepCloneWithContext(CloningContext ctx, T original)
		throws CloneException
	{
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) original.getClass();

		if(pretty) System.out.print(ctx.level() + clazz.getSimpleName() + ": " + original);
		T clone = ctx.getReference(original);
		if(clone != null)
		{
			if(pretty) System.out.println(" *");
			return clone;
		}
		if(pretty) System.out.println("");
		
		clone = isImmutable(ctx, original);
		if(clone != null)
		{
			return clone;
		}
		
		clone = handle(ctx, original);
		if(clone != null)
		{
			ctx.addReference(original, clone);
			return clone;
		}
		
		if(clazz.isArray())
		{
			clone = cloneArray(ctx, original);
			if(clone != null)
			{
				ctx.addReference(original, clone);
				return clone;
			}
		}
		
		clone = tryFastClone(ctx, original);
		if(clone != null)
		{
			ctx.addReference(original, clone);
			return clone;
		}
		
		clone = newInstanceOf(clazz);
		List<Field> fields = FieldUtils.getAllFields(clazz);
		
		ctx.increment();
		for (Field field : fields)
		{
			cloneField(ctx, field, original, clone);
		}
		ctx.decrement();

		ctx.addReference(original, clone);
		return clone;
	}
	
	protected <T> T isImmutable(CloningContext ctx, T t)
	{
		Class<?> clazz = t.getClass();
		if(clazz.isEnum())
		{
			return t;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T handle(CloningContext ctx, T original)
	{
		Class<T> clazz = (Class<T>) original.getClass();
		ReferenceHandler handler = handlerByType.get(clazz);
		if(handler == null)
		{
			return null;
		}
		return (T) handler.handle(original);
	}

	protected <T> void cloneField(CloningContext ctx, Field field, T original, T clone)
	{
		int modifiers = field.getModifiers();
		boolean isTransient = Modifier.isTransient(modifiers);
		boolean isStatic = Modifier.isStatic(modifiers);
		if(isTransient || isStatic)
		{
			return;
		}
		
		boolean isPrimitive = field.getType().isPrimitive();
		boolean tryPrimitives = false;
		if(isPrimitive && tryPrimitives)
		{
			FieldUtils.copyPrimitive(field, original, clone);
		}
		else
		{
			Object value = FieldUtils.valueFrom(field, original);
			Object clonedValueForField = value == null ? null : deepCloneWithContext(ctx, value);
			FieldUtils.set(field, clone, clonedValueForField);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T tryFastClone(CloningContext ctx, T original)
	{
		Class<T> clazz = (Class<T>) original.getClass();
		FastCloner<T> fast = (FastCloner<T>) fastClonerByTargetType.get(clazz);
		if(fast == null)
		{
			return null;
		}
		
		return fast.clone(ctx, original, this);
	}

	@SuppressWarnings("unchecked")
	private <T> T cloneArray(CloningContext ctx, T t)
	{
		ctx.increment();
		Class<?> clazz = t.getClass();
		int length = Array.getLength(t);
		T newInstance = (T) Array.newInstance(clazz.getComponentType(), length);
		for (int i = 0; i < length; i++)
		{
		        Object item = Array.get(t, i);
		        Object clone = deepCloneWithContext(ctx, item);
		        Array.set(newInstance, i, clone);
		}
		ctx.decrement();
		return newInstance;
	}
	
	protected <T> T newInstanceOf(Class<T> clazz)
		throws CloneException
	{
		return SilentObjectCreator.create(clazz);
//		try
//		{
//			return clazz.newInstance();
//		}
//		catch (Throwable t)
//		{
//			throw new CloneException("Error creating new instance of '" + clazz + "'", t);
//		}
	}

	@Override
	public void addHandler(Class<?> clazz, ReferenceHandler handler)
	{
		handlerByType.put(clazz, handler);
	}
}
