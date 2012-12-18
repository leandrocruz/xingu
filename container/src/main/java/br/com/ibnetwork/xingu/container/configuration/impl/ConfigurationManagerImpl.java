package br.com.ibnetwork.xingu.container.configuration.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FSUtils;

public class ConfigurationManagerImpl
    implements ConfigurationManager, Configurable, Initializable
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private DefaultConfigurationBuilder builder;
    
    private File rootDir;
    
    private String suffix;
    
    private Map<String, Configuration> registry;
    
    public void configure(Configuration conf) 
    	throws ConfigurationException
    {
        String rootDirName = conf.getChild("rootDir").getAttribute("name","pulga");
        rootDirName = FSUtils.load(rootDirName);
        logger.info("Configuration root dir is: "+rootDirName);
        if(rootDirName != null)
        {
            rootDir = new File(rootDirName);
        }
        suffix = conf.getChild("files").getAttribute("suffix","xml");
    }

    public void initialize() 
    	throws Exception
    {
        builder = new DefaultConfigurationBuilder();
    	if(rootDir == null)
    	{
    		registry = new HashMap<String, Configuration>();
    		return;
    	}
        IOFileFilter filter = FileFilterUtils.suffixFileFilter(suffix);
        File[] files = rootDir.listFiles((FileFilter)filter);
        if(files == null)
        {
            logger.info("No configuration files found at ["+this.rootDir+"]");
            registry = new HashMap<String, Configuration>();
            return;
        }
        registry = new HashMap<String, Configuration>(files.length * 10);
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            logger.info("Reading configuration from file: "+file);
            Configuration root = builder.buildFromFile(file);
            Configuration[] conf = root.getChildren("configuration");
            for (int j = 0; j < conf.length; j++)
            {
                Configuration configuration = conf[j];
                String key = configuration.getAttribute("key",null); 
                if(key == null || registry.containsKey(key))
                {
                    logger.warn("configuration key not found or duplicated. file is ["+file+"]");
                }
                else
                {
                    registry.put(key,configuration);                    
                }
            }
        }
    }
    
    public Configuration configurationFor(String roleName)
            throws ConfigurationException
    {
        return configurationFor(roleName, null);
    }

    public Configuration configurationFor(Class<?> clazz)
            throws ConfigurationException
    {
        return configurationFor(clazz.getName());
    }

    @Override
    public Configuration configurationFor(String roleName, String key)
            throws ConfigurationException
    {
        if(!StringUtils.isEmpty(key))
        {
            roleName +=":"+key;
        }
        return registry.get(key);
    }

    @Override
    public Configuration configurationFor(Class<?> role, String key)
        throws ConfigurationException
    {
        return configurationFor(role.getName(), key);
    }

    @Override
    public void register(String roleName, String key, Configuration conf)
    {
        if(!StringUtils.isEmpty(key))
        {
            roleName +=":"+key;
        }
        Configuration old = registry.get(roleName);
        Configuration[] children = conf.getChildren();
        if(old != null && old.getChildren() != null && children != null)
        {
            throw new NotImplementedYet("Configuration merge not implemented");
        }
        registry.put(roleName, conf);
    }


}
