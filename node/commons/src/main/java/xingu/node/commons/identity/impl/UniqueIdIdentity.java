package xingu.node.commons.identity.impl;

import xingu.node.commons.identity.Identity;

public class UniqueIdIdentity
	implements Identity<Long>
{
	private long id;

	public UniqueIdIdentity()
	{}

	public UniqueIdIdentity(long id)
	{
		this.id = id;
	}

	@Override
	public Long get()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return "#" + id;
	}

	public long getId()
	{
		return id;
	}
}
