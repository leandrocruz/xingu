package br.com.ibnetwork.xingu.store.impl.memory;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.store.StoreException;

public class InMemoryObjectStore
    implements ObjectStore
{
    private List<PersistentBean> pojos = new ArrayList<PersistentBean>();

    private long lastId = 0;

    @Override
    public <POJO extends PersistentBean> int store(POJO pojo)
        throws StoreException
    {
        if (pojo.getId() == 0)
        {
            pojo.setId(++lastId);
            pojos.add(pojo);
        }
        return 0;
    }

    @Override
    public <POJO extends PersistentBean> int delete(POJO pojo)
        throws StoreException
    {
        PersistentBean aux = null;
        for(PersistentBean p : pojos)
            if(p.getId()==pojo.getId())
            {
                aux = p;
                break;
            }
        
        if(aux != null)
            pojos.remove(aux);
        return 0;
    }

    @Override
    public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass,
        POJO pojo)
        throws StoreException
    {
        return delete(pojo);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
        throws StoreException
    {
        ArrayList<POJO> list = new ArrayList<POJO>();
        for (PersistentBean pojo : pojos)
        {
            if (pojoClass.isInstance(pojo))
                list.add((POJO) pojo);
        }
        return list;
    }

    @Override
    public <POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass,
        long id)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> void insert(POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> void insert(Class<POJO> pojoClass,
        POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int store(Class<POJO> pojoClass,
        POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int update(POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public <POJO extends PersistentBean> int update(Class<POJO> pojoClass,
        POJO pojo)
        throws StoreException
    {
        throw new NotImplementedYet();
    }

    @Override
    public void clear() 
        throws StoreException
    {
        pojos = new ArrayList<PersistentBean>();
        lastId = 0;
    }

    @Override
    public void takeSnapshot() 
        throws StoreException
    {
        throw new NotImplementedYet("TODO");
    }

}
