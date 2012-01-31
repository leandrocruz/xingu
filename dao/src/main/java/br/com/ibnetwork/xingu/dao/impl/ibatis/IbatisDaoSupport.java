package br.com.ibnetwork.xingu.dao.impl.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.dao.DaoException;
import br.com.ibnetwork.xingu.ibatis.Ibatis;

import com.ibatis.sqlmap.client.SqlMapException;

public abstract class IbatisDaoSupport<ID,T>
    implements Dao<ID,T>
{
	@Inject
	protected Ibatis iBatis;

	private String nameSpace;
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * iBatis nameSpace
	 */
	protected String getNameSpace()
	{
		if(nameSpace == null)
		{
			nameSpace = getClass().getSimpleName(); 
			nameSpace = nameSpace.substring(0,nameSpace.indexOf("Dao"));
			nameSpace = StringUtils.uncapitalize(nameSpace);
		}
		return nameSpace;
	}

	public final T getById(ID id) 
		throws DaoException
	{
		return queryObject("getById", id);
	}
	
	public final List<T> getAll() 
		throws DaoException
	{
		return queryList("getAll", null);
	}

	@SuppressWarnings("unchecked")
    protected T queryObject(String stmtId, Object param)
		throws DaoException
	{
		try
        {
			T obj = (T) iBatis.getSqlMap().queryForObject(getNameSpace()+"."+stmtId,param);
	        return obj;
        }
		catch(SqlMapException e)
		{
			log.error("dao."+stmtId+" error",e);
			return null;
		}
        catch (SQLException e)
        {
        	throw new DaoException("dao."+stmtId+" error",e);
        }
	}

    @SuppressWarnings("unchecked")
    protected List<T> queryList(String stmtId, Object param)
		throws DaoException
	{
		try
		{
			List<T> list = iBatis.getSqlMap().queryForList(getNameSpace()+"."+stmtId,param);
			return list;
		}
		catch(SqlMapException e)
		{
			log.error("dao."+stmtId+" error",e);
			return null;
		}
		catch (SQLException e)
		{
			throw new DaoException("dao."+stmtId+" error",e);
		}
	}
	
	public final int delete(ID obj) 
		throws DaoException
	{
		try
        {
			int count = iBatis.getSqlMap().delete(getNameSpace()+".delete",obj);
			return count;
        }
		catch(SqlMapException e)
		{
			log.error("dao.delete() error",e);
			return 0;
		}
        catch (SQLException e)
        {
        	throw new DaoException("dao.delete() error",e);
        }
	}

	public final void insert(T obj) 
		throws DaoException
	{
		try
        {
			iBatis.getSqlMap().insert(getNameSpace()+".insert",obj);
        }
		catch(SqlMapException e)
		{
			log.error("dao.insert() error",e);
		}
        catch (SQLException e)
        {
        	throw new DaoException("dao.insert() error",e);
        }
	}

	public final int update(T obj) 
		throws DaoException
	{
		try
		{
			int count = iBatis.getSqlMap().update(getNameSpace()+".update",obj);
			return count;
		}
		catch(SqlMapException e)
		{
			log.error("dao.update() error",e);
			return 0;
		}
		catch (SQLException e)
		{
			throw new DaoException("dao.update() error",e);
		}
	}
}