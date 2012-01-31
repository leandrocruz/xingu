package br.com.ibnetwork.xingu.store.impl.prevayler;

import java.util.Date;

import org.prevayler.Transaction;

import br.com.ibnetwork.xingu.store.PersistentBean;

public class AddToCacheTransaction
    implements Transaction
{
    private static final long serialVersionUID = 1L;
    
    private PersistentBean _bean;
	
	public AddToCacheTransaction(PersistentBean bean)
	{
		_bean = bean;
	}

	public void executeOn(Object prevalentSystem, Date executionTime)
	{
	    Cache cache = (Cache) prevalentSystem;
		cache.add(_bean);
	}
}