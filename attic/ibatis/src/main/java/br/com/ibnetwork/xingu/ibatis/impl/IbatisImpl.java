package br.com.ibnetwork.xingu.ibatis.impl;

import java.io.FileReader;
import java.io.Reader;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.ibatis.Ibatis;
import br.com.ibnetwork.xingu.utils.FSUtils;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;


public class IbatisImpl
    implements Ibatis, Configurable, Initializable
{
	private static Logger log = LoggerFactory.getLogger(IbatisImpl.class);
	
    private SqlMapClient sqlMap;

    private String file;
    
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        file = conf.getChild("ibatis").getAttribute("file","sqlMap.xml");
        file = FSUtils.load(file);
    }

    public void initialize() 
        throws Exception
    {
    	log.info("loading sqlMap: "+file);
    	if(file != null)
    	{
    		Reader reader = new FileReader(file);
    		sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
    	}
    }
    
    public SqlMapClient getSqlMap()
    {
        return sqlMap;
    }

}
