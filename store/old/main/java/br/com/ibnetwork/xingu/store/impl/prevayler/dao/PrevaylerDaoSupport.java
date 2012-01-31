package br.com.ibnetwork.xingu.store.impl.prevayler.dao;

import java.util.List;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.dao.DaoException;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.PersistentBean;

public abstract class PrevaylerDaoSupport<T extends PersistentBean>
    implements Dao<Long, T>
{
    @Dependency
    protected transient ObjectStore store;
    
    protected abstract Class<T> getBeanClass();

    public int delete(Long id) 
        throws DaoException
    {
        PersistentBean bean = getById(id);
        return store.delete(bean);
    }

    public List<T> getAll() 
        throws DaoException
    {
        Class<T> clazz = getBeanClass();
        return store.getAll(clazz);
    }

    public T getById(Long id) 
        throws DaoException
    {
        Class<T> clazz = getBeanClass();
        return store.getById(clazz, id);
    }

    public void insert(T obj) 
        throws DaoException
    {
        store.insert(obj);
    }

    public int update(T obj) 
        throws DaoException
    {
        return store.update(obj);
    }
}
