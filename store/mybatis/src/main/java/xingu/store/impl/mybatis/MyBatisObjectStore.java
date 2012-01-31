package xingu.store.impl.mybatis;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.store.ObjectStore;
import xingu.store.PersistentBean;
import xingu.store.StoreException;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class MyBatisObjectStore
	implements ObjectStore, Configurable, Startable
{
	@Inject
	private Factory factory;
	
	/*
	 * Once created, the SqlSessionFactory should exist for the duration of your application execution
	 */
	protected SqlSessionFactory sessionFactory;

	private String configurationFile;
	
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String environment;
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        configurationFile = conf.getChild("mybatis").getAttribute("conf", "mybatis.xml");
        environment = conf.getChild("mybatis").getAttribute("environment", null);
    }
    
	@Override
	public void start()
		throws Exception
	{
	    URL url = Resources.getResourceURL(configurationFile);
	    Reader reader = new FileReader(new File(url.getFile()));
	    if(StringUtils.isNotEmpty(environment))
	    {
	        sessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
	    }
	    else
	    {
	        sessionFactory = new SqlSessionFactoryBuilder().build(reader);
	    }
        org.apache.ibatis.session.Configuration conf = sessionFactory.getConfiguration();
        conf.setObjectFactory(new XinguObjectFactory(factory));
	}

	@Override
	public void stop()
		throws Exception 
	{}

	@SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> POJO getById(Class<POJO> clazz, long id)
        throws StoreException
    {
	    String namespace = clazz.getName();
	    SqlSession session = sessionFactory.openSession();
	    Object pojo = session.selectOne(namespace + ".getById", id);
        return (POJO) pojo;
    }

    @Override
    public <POJO extends PersistentBean> int delete(POJO pojo)
        throws StoreException
    {
        @SuppressWarnings("unchecked")
        Class<POJO> clazz = (Class<POJO>) pojo.getClass();
        return delete(clazz, pojo);
    }

    @Override
    public <POJO extends PersistentBean> int delete(Class<POJO> clazz, POJO pojo)
        throws StoreException
    {
        /*
         * Each thread should have its own instance of SqlSession
         */
        String namespace = clazz.getName();
        long id = pojo.getId();
        SqlSession session = openSession();
        String statement = namespace + ".del";

        try
        {
            return session.delete(statement, id);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement + ", id=" + id;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally 
        {
            session.close();
        }
    }

    private SqlSession openSession()
    {
        SqlSession session = sessionFactory.openSession(true);
        return session;
    }

    @Override
    public <POJO extends PersistentBean> int store(POJO pojo)
        throws StoreException
    {
        @SuppressWarnings("unchecked")
        Class<POJO> clazz = (Class<POJO>) pojo.getClass();
        return store(clazz, pojo);
    }

    @Override
    public <POJO extends PersistentBean> int store(Class<POJO> clazz, POJO pojo)
            throws StoreException
    {
        /*
         * Each thread should have its own instance of SqlSession
         */
        String namespace = clazz.getName();
        long id = pojo.getId();
        SqlSession session = openSession();
        String statement = null;

        int result;
        try
        {
            if(id <= 0)
            {
                statement = namespace + ".add";
                result = session.insert(statement, pojo);
            }
            else
            {
                statement = namespace + ".update";
                result = session.update(statement, pojo);
            }
            return result;
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement + ", id=" + id;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally 
        {
            session.close();
        }
    }
    
    public <POJO extends PersistentBean> int insertWithId(POJO pojo)
        throws StoreException
    {
        @SuppressWarnings("unchecked")
        Class<POJO> clazz = (Class<POJO>) pojo.getClass();
        return insertWithId(clazz, pojo);
    }

    public <POJO extends PersistentBean> int insertWithId(Class<POJO> clazz, POJO pojo)
            throws StoreException
    {
        String namespace = clazz.getName();
        SqlSession session = openSession();
        String statement = namespace + ".insertWithId";

        int result;
        try
        {
            result = session.insert(statement, pojo);
            return result;
        }
        catch (Throwable t)
        {
            String msg = "Error executing sql statement " + statement + ", id=" + pojo.getId();
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> POJO selectOne(String statement)
        throws StoreException
    {
        SqlSession session = openSession();
        try
        {
            return (POJO) session.selectOne(statement);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> POJO selectOne(String statement, Object param)
        throws StoreException
    {
        SqlSession session = openSession();
        try
        {
            return (POJO) session.selectOne(statement, param);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement + " param: " + param;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> List<POJO> selectList(String statement)
        throws StoreException
    {
        SqlSession session = openSession();
        try
        {
            return session.selectList(statement);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> List<POJO> selectList(String statement, Object param)
        throws StoreException
    {
        SqlSession session = openSession();
        try
        {
            return session.selectList(statement, param);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement + " param: " + param;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @Override
    public void clear()
        throws StoreException
    {
        throw new NotImplementedYet("TODO");
    }

    @Override
    public void takeSnapshot()
        throws StoreException
    {
        throw new NotImplementedYet("TODO");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> clazz)
        throws StoreException
    {
        String namespace = clazz.getName();
        SqlSession session = openSession();
        String statement = namespace + ".getAll";
        
        try
        {
            return session.selectList(statement);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement;
            logger.error(msg, t);
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }
}