package xingu.node.commons.sandbox.impl;

import xingu.container.Container;
import xingu.node.commons.sandbox.Sandbox;
import xingu.utils.classloader.NamedClassLoader;

public class SandboxImpl
	implements Sandbox
{
	private String				id;

	private NamedClassLoader	cl;

	private Container			container;
	
	public SandboxImpl(String id, Container container, NamedClassLoader cl)
	{
		this.id        = id;
		this.cl        = cl;
		this.container = container;
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public NamedClassLoader classLoader()
	{
		return cl;
	}

	@Override
	public Container container()
	{
		return container;
	}

	@Override
	public String toString()
	{
		return "id#" + id + " identity#" + (System.identityHashCode(this));
	}

	@Override
	public Object get(String component)
		throws Exception
	{
		Class<?> clazz = cl.loadClass(component);
		return container.lookup(clazz);
	}
}