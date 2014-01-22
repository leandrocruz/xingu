package br.com.ibnetwork.xingu.dao;

public interface DaoManager
{
	String ROLE = DaoManager.class.getName();

	<DAO extends Dao> DAO getDao(Class clazz) 
		throws DaoException;
}
