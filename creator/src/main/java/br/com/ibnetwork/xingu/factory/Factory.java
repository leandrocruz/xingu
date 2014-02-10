package br.com.ibnetwork.xingu.factory;

import org.apache.avalon.framework.configuration.Configuration;

public interface Factory
{
	Object create(String className)
		throws FactoryException;

	Object create(String className, ClassLoader cl)
		throws FactoryException;

	Object create(String className, Configuration conf)
		throws FactoryException;

	Object create(String className, Configuration conf, ClassLoader cl)
		throws FactoryException;

	Object create(String className, Object... params)
		throws FactoryException;

	Object create(String className, ClassLoader cl, Object... params)
		throws FactoryException;

	Object create(String className, Configuration conf, Object... params)
		throws FactoryException;

	Object create(String className, Configuration conf, ClassLoader cl, Object... params)
		throws FactoryException;

	<T> T create(Class<? extends T> clazz)
		throws FactoryException;

	<T> T create(Class<? extends T> clazz, Configuration conf)
		throws FactoryException;

	<T> T create(Class<? extends T> clazz, Object... params)
		throws FactoryException;

	<T> T create(Class<? extends T> clazz, Configuration conf, Object... params)
		throws FactoryException;
}
