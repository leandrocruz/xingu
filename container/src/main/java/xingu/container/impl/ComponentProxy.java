package xingu.container.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Container;

public class ComponentProxy
	implements InvocationHandler
{
	private final Container container;

	private final Class<?> type;
	
	private final String key;
	
	private boolean resolved = false;
	
	private Object delegate;

	private static Logger log = LoggerFactory.getLogger(ComponentProxy.class);

	public ComponentProxy(Container container, Class<?> type, String key)
	{
		this.container = container;
		this.type = type;
		this.key = key;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable
	{
		String methodName = method.getName();
		if(!resolved && "toString".equals(methodName))
		{
			return "Proxy for: '" + type.getName() + "'";
		}
		
		synchronized (this)
		{
			if(!resolved)
			{
				log.info("Late initialization for '{}'", type.getName());
				delegate = container.lookup(type, key);
				resolved = true;
			}
		}
		
		Object result = method.invoke(delegate, args);
		return result;
	}

	public static Object newProxyFor(Container container, Object owner, Class<?> type, String value)
	{
		log.info("Creating proxy '{}' for '{}'", type.getName(), owner.getClass().getName());
		ClassLoader cl = owner.getClass().getClassLoader();
		return Proxy.newProxyInstance(cl, new Class<?>[]{type}, new ComponentProxy(container, type, value));
	}
}

