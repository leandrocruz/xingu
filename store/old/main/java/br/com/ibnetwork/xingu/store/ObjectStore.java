package br.com.ibnetwork.xingu.store;


import java.util.List;


public interface ObjectStore
{
	<POJO extends PersistentBean> POJO getById(Class<POJO> pojoClass, long id)
		throws StoreException;
	
	<POJO extends PersistentBean> List<POJO> getAll(Class<POJO> pojoClass)
		throws StoreException;

	<POJO extends PersistentBean> int store(POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> int store(Class<POJO> pojoClass, POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> void insert(POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> void insert(Class<POJO> pojoClass, POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> int update(POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> int update(Class<POJO> pojoClass, POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> int delete(POJO pojo)
		throws StoreException;

	<POJO extends PersistentBean> int delete(Class<POJO> pojoClass, POJO pojo)
	    throws StoreException;

	void clear()
	    throws StoreException;

	void takeSnapshot()
	    throws StoreException;
	
}
