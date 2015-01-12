package xingu.utils.clone.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import xingu.lang.SilentObjectCreator;
import xingu.utils.FieldUtils;
import xingu.utils.clone.CloneException;
import xingu.utils.clone.Cloner;
import xingu.utils.clone.CloningContext;
import xingu.utils.clone.FastCloner;
import xingu.utils.clone.ReferenceHandler;
import xingu.utils.clone.impl.fast.ArrayListFastCloner;
import xingu.utils.clone.impl.fast.BooleanFastCloner;
import xingu.utils.clone.impl.fast.ByteFastCloner;
import xingu.utils.clone.impl.fast.CharFastCloner;
import xingu.utils.clone.impl.fast.DateFastCloner;
import xingu.utils.clone.impl.fast.DoubleFastCloner;
import xingu.utils.clone.impl.fast.FloatFastCloner;
import xingu.utils.clone.impl.fast.HashMapFastCloner;
import xingu.utils.clone.impl.fast.HashSetFastCloner;
import xingu.utils.clone.impl.fast.IntegerFastCloner;
import xingu.utils.clone.impl.fast.LongFastCloner;
import xingu.utils.clone.impl.fast.ObjectFastCloner;
import xingu.utils.clone.impl.fast.ShortFastCloner;
import xingu.utils.clone.impl.fast.StringFastCloner;
import xingu.utils.clone.impl.fast.TreeSetFastCloner;

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
		fastClonerByTargetType.put(Date.class, new DateFastCloner());
		fastClonerByTargetType.put(ArrayList.class, new ArrayListFastCloner());
		fastClonerByTargetType.put(HashMap.class, new HashMapFastCloner());
		fastClonerByTargetType.put(TreeSet.class, new TreeSetFastCloner());
		fastClonerByTargetType.put(HashSet.class, new HashSetFastCloner());
	}

	@Override
	public <T> T deepClone(T original)
		throws CloneException
	{
		CloningContext ctx = new CloningContext(pretty);
		ctx.start();
		T t = deepCloneWithContext(ctx, original);
		ctx.end();
		return t;
	}
	
	protected <T> T handleExceptions(CloningContext ctx, T original)
	{
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) original.getClass();

		if(Proxy.isProxyClass(clazz))
		{
			return original;
		}
		
		boolean isMock = isMock(original);
		if(isMock)
		{
			return original;
		}
		
		T clone = ctx.getReference(original);
		if(clone != null)
		{
			return clone;
		}
		
		clone = isImmutable(ctx, original);
		if(clone != null)
		{
			return clone;
		}
		
		clone = tryHandler(ctx, original);
		if(clone != null)
		{
			ctx.addReference(original, clone);
			return clone;
		}
		
		clone = tryFastClone(ctx, original);
		if(clone != null)
		{
			ctx.addReference(original, clone);
			return clone;
		}

		return null;

		
	}
	@Override
	public <T> T deepCloneWithContext(CloningContext ctx, T original)
		throws CloneException
	{
		ctx.print(original);

		T clone = handleExceptions(ctx, original);
		if(clone != null)
		{
			return clone;
		}

		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) original.getClass();
		if(clazz.isArray())
		{
			clone = cloneArray(ctx, original);
			if(clone != null)
			{
				ctx.addReference(original, clone);
				return clone;
			}
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
	
	protected boolean isMock(Object object)
	{
		return false;
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
	protected <T> T tryHandler(CloningContext ctx, T original)
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
			Object clonedValueForField = value == null ? null : deepCloneWithContext(ctx.setName(field.getName()), value);
			FieldUtils.set(field, clone, clonedValueForField);
		}
		ctx.clearName();
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
		        ctx.setName("["+i+"]");
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
