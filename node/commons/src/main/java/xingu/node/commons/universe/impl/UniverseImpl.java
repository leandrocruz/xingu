package xingu.node.commons.universe.impl;

import xingu.node.commons.universe.Universe;
import br.com.ibnetwork.xingu.container.Container;

public class UniverseImpl
	implements Universe
{
	private String		id;

	private ClassLoader	cl;

	private Container	container;
	
	public UniverseImpl(String id, Container container, ClassLoader cl)
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
}
