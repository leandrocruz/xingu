package xingu.store.impl.mybatis;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.store.PersistentBean;
import xingu.store.StoreException;
import xingu.store.impl.BadObjectStore;

public class MyBatisObjectStore
	extends BadObjectStore
	implements Configurable, Startable
{
	@Inject
	private Factory factory;
	
	/*
	 * Once created, the SqlSessionFactory should exist for the duration of your application execution
	 */
	protected SqlSessionFactory sessionFactory;

	private String configurationFile;
	
    private String environment;

    Properties properties = new Properties();

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
    	Configuration mybatisConfig = conf.getChild("mybatis");
        configurationFile = mybatisConfig.getAttribute("conf", "mybatis.xml");
        environment = mybatisConfig.getAttribute("environment", null);

        
        Configuration dataSourceConfig[] = mybatisConfig.getChildren();
        for (Configuration configuration : dataSourceConfig)
		{
			String name = configuration.getAttribute("name");
			String value = configuration.getAttribute("value");
			properties.setProperty(name, value);
		}
    }
    
	@Override
	public void start()
		throws Exception
	{
	    URL url = Resources.getResourceURL(configurationFile);
	    Reader reader = new FileReader(new File(url.getFile()));
	    if(StringUtils.isNotEmpty(environment))
	    {
	        sessionFactory = new SqlSessionFactoryBuilder().build(reader, environment, properties);
	    }
	    else
	    {
	        sessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
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
    	return delete(clazz, pojo.getId());
    }
    
	@Override
	public <POJO extends PersistentBean> int delete(Class<POJO> clazz, long id)
		throws StoreException
	{
        /*
         * Each thread should have its own instance of SqlSession
         */
        String namespace = clazz.getName();
        SqlSession session = openSession();
        String statement = namespace + ".del";

        try
        {
            return session.delete(statement, id);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement " + statement + ", id=" + id;
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
            String msg = "Error executing sql statement '" + statement + "', id=" + id;
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
            String msg = "Error executing sql statement '" + statement + "', id=" + pojo.getId();
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
            String msg = "Error executing sql statement '" + statement + "'";
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
            String msg = "Error executing sql statement '" + statement + "' param: " + param;
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }

    @Override
    public <POJO extends PersistentBean> List<POJO> getAll(Class<POJO> clazz)
        throws StoreException
    {
        String namespace = clazz.getName();
        String statement = namespace + ".getAll";
        return selectList(statement);
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
            String msg = "Error executing sql statement '" + statement + "'";
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
            String msg = "Error executing sql statement '" + statement + "' param: " + param;
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }
    
    @Override
    public int run(String statement, Object param)
        throws StoreException
    {
        SqlSession session = openSession();
        try
        {
            return session.insert(statement, param);
        }
        catch(Throwable t)
        {
            String msg = "Error executing sql statement '" + statement + "' param: " + param;
            throw new StoreException(msg, t);
        }
        finally
        {
            session.close();
        }
    }
}