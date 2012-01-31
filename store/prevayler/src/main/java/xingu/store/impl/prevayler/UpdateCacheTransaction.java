package xingu.store.impl.prevayler;

import java.util.Date;

import org.prevayler.Transaction;

import xingu.store.PersistentBean;

public class UpdateCacheTransaction
    implements Transaction
{
    private static final long serialVersionUID = 1L;
    
    private PersistentBean _bean;
	
	public UpdateCacheTransaction(PersistentBean bean)
	{
		_bean = bean;
	}

	public void executeOn(Object prevalentSystem, Date executionTime)
	{
	    Cache cache = (Cache) prevalentSystem;
		cache.remove(_bean);
		cache.add(_bean);
	}
}