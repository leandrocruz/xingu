package br.com.ibnetwork.xingu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.dao.DaoException;
import br.com.ibnetwork.xingu.dao.DaoManager;
import br.com.ibnetwork.xingu.dao.impl.ibatis.GenericDao;
import br.com.ibnetwork.xingu.factory.Factory;

public class DaoManagerImpl
    implements DaoManager, Configurable
{
	private static Logger log = LoggerFactory.getLogger(DaoManagerImpl.class);
	
	private Map<String,Object> map = new HashMap<String,Object>(); 
	
	@Inject
	private Factory factory;
	
	private Class genericDaoClass;
	
	public void configure(Configuration conf) 
		throws ConfigurationException
    {
		String genericDaoClassName = conf.getAttribute("genericDaoClass",GenericDao.class.getName());
		try
        {
	        genericDaoClass = ClassUtils.getClass(genericDaoClassName);
        }
        catch (ClassNotFoundException e)
        {
        	throw new ConfigurationException("Error loading generic dao class",e);
        }
		Configuration[] daos = conf.getChild("daoConfig").getChildren("dao");
		for (Configuration daoConf : daos)
        {
	        String className = daoConf.getAttribute("class");
	        String daoClassName = daoConf.getAttribute("daoClass");
	        map.put(className, daoClassName);
        }
    }

	@SuppressWarnings("unchecked")
	public <DAO extends Dao>  DAO getDao(Class clazz) 
		throws DaoException
	{
		String key = clazz.getName();
		Object dao = map.get(key);
		if(dao == null)
		{
			//try to apply default procedure
			String packageName = ClassUtils.getPackageName(clazz);
			String shortName = ClassUtils.getShortClassName(clazz);
			String daoClassName = packageName + ".dao." + shortName + "Dao";
			try
			{
				dao = factory.create(daoClassName);	
			}
			catch(Throwable t)
			{
				//return generic dao
				log.info("Can't find dao for class: "+clazz.getName()+" returning generic dao: "+genericDaoClass.getName());
				dao = (Dao) factory.create(genericDaoClass, new Object[]{clazz});
			}
			map.put(key, dao);
			return (DAO) dao;
		}
		if(dao instanceof String)
		{
			DAO result = (DAO) factory.create((String)dao);
			map.put(key, result);
			return result;
		}
		return (DAO) dao;
	}
}
