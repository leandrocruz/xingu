package xingu.store.impl;

import java.util.List;

import xingu.store.ObjectStore;
import xingu.store.PersistentBean;
import xingu.store.StoreException;
import xingu.store.impl.mybatis.MyBatisObjectStore;

public class FlexibleObjectStore
	extends MyBatisObjectStore
	implements ObjectStore
{

	@Override
	public <POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass, long id)
		throws StoreException
	{
		return null;
	}

	@Override
	public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
		throws StoreException
	{
		return null;
	}

	@Override
	public <POJO extends PersistentBean> int store(POJO pojo)
		throws StoreException
	{
		return 0;
	}

	@Override
	public <POJO extends PersistentBean> int store(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
	{
		return 0;
	}

	@Override
	public <POJO extends PersistentBean> int delete(POJO pojo)
		throws StoreException
	{
		return 0;
	}

	@Override
	public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
	{
		return 0;
	}

	@Override
	public <POJO extends PersistentBean> POJO selectOne(String statement)
		throws StoreException
	{
		return null;
	}

	@Override
	public <POJO extends PersistentBean> POJO selectOne(String statement,
			Object param) throws StoreException
	{
		return null;
	}

	@Override
	public <POJO extends PersistentBean> List<POJO> selectList(String statement)
		throws StoreException
	{
		return null;
	}

	@Override
	public <POJO extends PersistentBean> List<POJO> selectList(String statement, Object param)
		throws StoreException
	{
		return null;
	}

	@Override
	public void clear()
		throws StoreException
	{}

	@Override
	public void takeSnapshot()
		throws StoreException
	{}

}
