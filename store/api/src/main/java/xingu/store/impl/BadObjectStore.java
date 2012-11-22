package xingu.store.impl;

import java.util.List;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

import xingu.store.ObjectStore;
import xingu.store.PersistentBean;
import xingu.store.StoreException;

public class BadObjectStore
	implements ObjectStore
{

	@Override
	public <POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass, long id)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> int store(POJO pojo)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> int store(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> int delete(POJO pojo)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> POJO selectOne(String statement)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> POJO selectOne(String statement, Object param)
		throws StoreException
	{
		throw new NotImplementedYet();
	}
	
	@Override
    public int run(String statement, Object param)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

	@Override
	public <POJO extends PersistentBean> List<POJO> selectList(String statement)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> List<POJO> selectList(String statement, Object param)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, long id)
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void clear()
		throws StoreException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void takeSnapshot()
		throws StoreException
	{
		throw new NotImplementedYet();
	}
}
