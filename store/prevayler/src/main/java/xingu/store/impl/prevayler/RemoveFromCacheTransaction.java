package xingu.store.impl.prevayler;

import java.util.Date;

import org.prevayler.Transaction;

import xingu.store.PersistentBean;

public class RemoveFromCacheTransaction
    implements Transaction
{
	private PersistentBean _bean;

	public RemoveFromCacheTransaction(PersistentBean bean)
	{
		_bean = bean;
	}

	public void executeOn(Object prevalentSystem, Date executionTime)
	{
		Cache cache = (Cache) prevalentSystem;
		cache.remove(_bean);
	}
}