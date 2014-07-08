package xingu.store.impl;

import xingu.store.PersistentBean;

public class BeanSupport
	implements PersistentBean
{
	protected long id;
	
	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public void setId(long id)
	{
		this.id = id;
	}
}
