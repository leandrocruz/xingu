package br.com.ibnetwork.xingu.store.impl;

import java.util.List;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.dao.DaoManager;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.store.StoreException;

public class ObjectStoreImpl
    implements ObjectStore
{
	@Dependency
	private DaoManager daoManager;
	
	private Dao getDao(Class beanClass)
	{
		Dao dao = daoManager.getDao(beanClass);
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public <POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass, long id)
		throws StoreException
	{
		return (POJO) getDao(pojoClass).getById(id);
	}

	@SuppressWarnings("unchecked")
	public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
		throws StoreException
    {
		return getDao(pojoClass).getAll();
    }

	@SuppressWarnings("unchecked")
	public <POJO extends PersistentBean> void insert(POJO bean)		
		throws StoreException
    {
		insert((Class<POJO>)bean.getClass(), bean);
    }
	
	public <POJO extends PersistentBean> void insert(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
    {
		if(pojo.getId() != 0)
		{
			throw new StoreException("ID should be 0 (zero)");
		}
		
		getDao(pojoClass).insert(pojo);
    }

	@SuppressWarnings("unchecked")
	public <POJO extends PersistentBean> int update(POJO pojo)
		throws StoreException
    {
	    return update((Class<POJO>)pojo.getClass(), pojo);
    }

	public <POJO extends PersistentBean> int update(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
    {
		return getDao(pojoClass).update(pojo);
    }
	
	@SuppressWarnings("unchecked")
	public <POJO extends PersistentBean> int delete(POJO pojo) 
		throws StoreException 
	{
	    return delete((Class<POJO>)pojo.getClass(), pojo);
    }

	public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo)
		throws StoreException
	{
		return getDao(pojoClass).delete(pojo.getId());
	}

	public <POJO extends PersistentBean> int store(POJO pojo)
        throws StoreException
    {
		if(pojo.getId() == 0)
		{
			insert(pojo);
			return 0;
		}
		else
		{
			return update(pojo);
		}
    }

	public <POJO extends PersistentBean> int store(Class<POJO> pojoClass, POJO pojo)
        throws StoreException
    {
		if(pojo.getId() == 0)
		{
			insert(pojoClass, pojo);
			return 0;
		}
		else
		{
			return update(pojoClass, pojo);
		}
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
        throw new NotImplementedYet("TODO");
    }

}