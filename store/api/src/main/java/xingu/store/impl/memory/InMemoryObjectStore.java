package xingu.store.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xingu.store.PersistentBean;
import xingu.store.StoreException;
import xingu.store.impl.BadObjectStore;

public class InMemoryObjectStore
	extends BadObjectStore
{
    private HashMap<String, List<PersistentBean>> hashListsPojos = new HashMap<String, List<PersistentBean>>();

    private HashMap<String, Integer> lastIds = new HashMap<String, Integer>();

    @Override
    public <POJO extends PersistentBean> int store(POJO pojo)
            throws StoreException
    {
        Class<POJO> clazz = (Class<POJO>) pojo.getClass();
        return store(clazz, pojo);
    }

    @Override
    public <POJO extends PersistentBean> int store(Class<POJO> pojoClass,
            POJO pojo) throws StoreException
    {
        List<PersistentBean> listPojos = getPojoList(pojoClass);

        if (pojo.getId() == 0)
        {
            int lastId = lastIds.get(pojoClass.getName());
            pojo.setId(++lastId);
            listPojos.add(pojo);
            lastIds.put(pojoClass.getName(), lastId);
        }
        else
        {
            // TODO: Falta fazer update?
        }
        return 0;
    }

    @Override
    public <POJO extends PersistentBean> int delete(POJO pojo)
        throws StoreException
    {
        Class<POJO> clazz = (Class<POJO>) pojo.getClass();
        return delete(clazz, pojo);
    }

    @Override
    public <POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo)
        throws StoreException
    {
        List<PersistentBean> listPojos = getPojoList(pojoClass);

        PersistentBean aux = null;
        for (PersistentBean p : listPojos)
            if (p.getId() == pojo.getId())
            {
                aux = p;
                break;
            }

        if (aux != null)
            listPojos.remove(aux);
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
            throws StoreException
    {
        List<PersistentBean> listPojos = getPojoList(pojoClass);

        ArrayList<POJO> list = new ArrayList<POJO>();
        for (PersistentBean pojo : listPojos)
        {
            if (pojoClass.isInstance(pojo))
                list.add((POJO) pojo);
        }
        return list;
    }

    @Override
    public <POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass,
            long id) throws StoreException
    {
        List<PersistentBean> listPojos = getPojoList(pojoClass);

        PersistentBean aux = null;
        for (PersistentBean p : listPojos)
        {
            if (id == p.getId())
            {
                aux = p;
                break;
            }
        }

        if (aux != null)
        {
            return (POJO) aux;
        }
        return null;
    }

    private <POJO> List<PersistentBean> getPojoList(Class<POJO> pojoClass)
    {
        String className = pojoClass.getName();
        List<PersistentBean> listPojos = hashListsPojos.get(className);
        if (listPojos == null)
        {
            listPojos = new ArrayList<PersistentBean>();
            hashListsPojos.put(className, listPojos);
            lastIds.put(className, 0);
        }
        return listPojos;
    }

    @Override
    public void clear()
        throws StoreException
    {
        hashListsPojos = new HashMap<String, List<PersistentBean>>();
        lastIds = new HashMap<String, Integer>();
    }
}
