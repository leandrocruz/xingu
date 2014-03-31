package xingu.node.commons.sandbox.impl;

import xingu.node.commons.sandbox.Sandbox;
import br.com.ibnetwork.xingu.container.Container;

public class SandboxImpl
	implements Sandbox
{
	private String		id;

	private ClassLoader	cl;

	private Container	container;
	
	public SandboxImpl(String id, Container container, ClassLoader cl)
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
	public ClassLoader classLoader()
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
		return "Universe '"+id+"' @ " + (System.identityHashCode(this));
	}

	@Override
	public Object get(String component)
		throws Exception
	{
		Class<?> clazz = cl.loadClass(component);
		return container.lookup(clazz);
	}
}
