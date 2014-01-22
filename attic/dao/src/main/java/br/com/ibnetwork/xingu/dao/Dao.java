package br.com.ibnetwork.xingu.dao;

import java.util.List;

public interface Dao<ID,T>
{
	T getById(ID id)
		throws DaoException;
	
	List<T> getAll()
		throws DaoException;
	
	void insert(T obj)
		throws DaoException;
		
	int update(T obj)
		throws DaoException;
	
	int delete(ID id)
		throws DaoException;
}
