package br.com.ibnetwork.xingu.attributes.impl.xml;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

//AVALON FRAMEWORK
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.configuration.DefaultConfigurationSerializer;

//COMMONS LOGGING
import org.apache.commons.io.FileUtils;

//XINGU ATTRIBUTES
import br.com.ibnetwork.xingu.attributes.Attribute;
import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.attributes.Repository;
import br.com.ibnetwork.xingu.attributes.UnparsableDate;
import br.com.ibnetwork.xingu.attributes.impl.AttributeListManagerSupport;

//XINGU OBJECT FACTORY 
import br.com.ibnetwork.xingu.factory.FactoryException;

//XINGU ID GENERATOR
import br.com.ibnetwork.xingu.utils.FSUtils;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public class AttributeListManagerXML
	extends AttributeListManagerSupport
	implements AttributeListManager, Configurable, Initializable
{
    private MessageFormat fileNameFormat;

    private Map<String,RepositoryGroup> groups;

	public void configure(Configuration conf) 
		throws ConfigurationException
	{
		String language = conf.getChild("fileName").getAttribute("locale-language");
		String country = conf.getChild("fileName").getAttribute("locale-country");
		Locale locale = new Locale(language,country);
		fileNameFormat = new MessageFormat(conf.getChild("fileName").getAttribute("format","{0}.xml"),locale);
		this.groups = new HashMap<String,RepositoryGroup>();
		Configuration[] groupsConf = conf.getChildren("groups");
		for (int i = 0; i < groupsConf.length; i++)
		{
			Configuration groupConf = groupsConf[i];
			RepositoryGroup group = new RepositoryGroup();
			group.configure(groupConf);
			String groupName = group.getName();
			groups.put(groupName, group);
		}
		
	}

	public void initialize() 
		throws Exception
	{
        super.initialize();
		for (Iterator iter = groups.keySet().iterator(); iter.hasNext();)
        {
            String key  = (String)  iter.next();
            RepositoryGroup group = groups.get(key);
            group.initialize();
        }
        
	}
	
    public AttributeList getListById(String groupName, String repoName, long id) 
		throws AttributeException 
	{
        if(id <=0)
        {
            log.info("Can't retrieve lists with id ["+id+"]");
            return null;
        }
		Repository repo = getRepositoryByName(groupName, repoName);
		Configuration conf = repo.getListConfiguration(id);
		AttributeList result = createListInstance(id,conf);
        String nameSpace = getNameSpace(groupName,repoName); 
        result.setNameSpace(nameSpace);
        return result;
	}

	public AttributeList[] queryList(String groupName, String repoName, Attribute attribute) 
		throws AttributeException
	{
		Repository repo = getRepositoryByName(groupName, repoName);
		List<Configuration> matching = repo.getMatchingConfigurations(attribute);
		AttributeList[] result = new AttributeList[matching.size()];
		int i=0;
		for (Iterator<Configuration> iter = matching.iterator(); iter.hasNext();)
        {
            Configuration conf = iter.next();
			long id = conf.getAttributeAsLong("id",-1);    
			result[i++] = createListInstance(id,conf);
        }
		return result;
	}
	
	private AttributeList createListInstance(long id, Configuration conf)
		throws AttributeException
	{
        Configuration[] attributes = conf.getChild("attributes").getChildren("attribute");
        List<Attribute> result = new ArrayList<Attribute>(attributes.length);
        if(attributes.length == 0)
        {
            log.warn("Creating list id["+id+"] with ZERO attributes");
        }
        for (int i = 0; i < attributes.length; i++)
        {
            Configuration attrConf = attributes[i];
            String typeName = attrConf.getAttribute("type",null);
            String storedValue = attrConf.getAttribute("value",null);
            try
            {
                Attribute attribute = create(typeName,storedValue);
                result.add(attribute);
            }
            catch (Exception e)
            {
                throw new FactoryException("Error getting attribute type using ["+typeName+"]",e);
            }
        }
        AttributeList attrList = create(id, result);
        return attrList;
	}

	public long store(String groupName, String repoName, AttributeList list) 
		throws AttributeException 
	{
        RepositoryGroup group = groups.get(groupName);
        long id = list.getId();
        if(id <= 0)
        {
            //new list
            id = setListId(list);    
        }
		Repository repo = getRepositoryByName(groupName, repoName); 
		try
		{
			return repo.store(list);	
		}
		catch(Exception e)
		{
			throw new AttributeException("Error storing list id["+list.getId()+"]",e);
		}
	}

    private Repository getRepositoryByName(String groupName, String repoName) 
		throws AttributeException
	{
		RepositoryGroup group = groups.get(groupName);
        try
        {
			Repository repo = group.getRepository(repoName);
			return repo;
        }
        catch (Exception e)
        {
			throw new AttributeException("Error getting repository["+repoName+"] on group["+groupName+"]",e);
        }
	}
	
	public void remove(String groupName, String repoName, long id)		
		throws AttributeException
	{
		Repository repo = getRepositoryByName(groupName, repoName);
		String repoFileName = repo.getFullPath(id);
		File repoFile = new File(repoFileName);
		if(!repoFile.delete())
		{
			throw new AttributeException("Error removing list id[" + repoFile + "]");
		}
	}

	private class RepositoryGroup
		implements Configurable,Initializable
	{

        private Map<String, Repository> repositories;

        private File baseDir;

        private String groupName;

        public void configure(Configuration conf) 
        	throws ConfigurationException
        {
        	String baseDirName =conf.getAttribute("baseDir","/tmp");
			baseDirName = FSUtils.load(baseDirName);
        	baseDir = new File(baseDirName);
			groupName = conf.getAttribute("groupName", DEFAULT_GROUP_NAME);
        }

        public Repository getRepository(String repoName)
        	throws Exception
        {
        	Repository repo = repositories.get(repoName);
        	if(repo == null)
        	{
        		log.debug("Creating repo ["+repoName+"] under ["+baseDir+"] on demand"); 
				repo = createRepository(repoName);
				repositories.put(repoName,repo);
        	}
        	return repo;
        }

        public String getName()
        {
			return groupName;
        }

        public void initialize() 
        	throws Exception
        {
			if(!baseDir.exists())
            {
			    log.warn("Creating dir ["+baseDir+"]");
                baseDir.mkdirs();
            }
            String[] repoNames = baseDir.list();
            
			repositories = new HashMap<String, Repository>();
			for (int j = 0; j < repoNames.length; j++)
			{
				String repoName = repoNames[j];
                Repository repo = createRepository(repoName);
				repositories.put(repoName, repo);				
			}
        }

        private Repository createRepository(String repoName) 
        	throws Exception
        {
            File repoDir = new File(baseDir + File.separator + repoName);
            if(!repoDir.exists())
            {
            	FileUtils.forceMkdir(repoDir);
            }
            log.debug("Creating repository name[" + repoName + "] on group ["+groupName+"] dir[" + repoDir + "]");
            RepositoryImpl repo = new RepositoryImpl(repoDir, repoName, groupName);								
            return repo;
        }
        
	}

	class RepositoryImpl
		implements Repository
	{
        private File dir;

		DefaultConfigurationBuilder builder;
		
		DefaultConfigurationSerializer serializer;

		String[] extensions = new String[]{"xml"};
		
		private String repoName;
		
		private String groupName;

        public RepositoryImpl(File repoDir, String repoName, String groupName)
        {
			this.dir = repoDir;
			this.groupName = groupName;
			this.repoName = repoName;
			this.builder = new DefaultConfigurationBuilder();
			Properties props = new Properties();
			props.put(javax.xml.transform.OutputKeys.ENCODING,"ISO-8859-1");
			this.serializer = new DefaultConfigurationSerializer();
			this.serializer.setIndent(true);
        }

        public long store(AttributeList list)
			throws Exception
        {
        	long id = list.getId();
			String fileName = getFileName(id);
			String pathName = dir + File.separator+ fileName;
			log.debug("Storing list id["+id+"] on ["+pathName+"]");
			Configuration conf = toConf(list);
        	serializer.serializeToFile(pathName,conf);
        	return id;
        }

		Configuration toConf(AttributeList list)
		{
			long id = list.getId();
			DefaultConfiguration result = new DefaultConfiguration("attributeList");
			result.setAttribute("id",Long.toString(id));
			DefaultConfiguration attributes = new DefaultConfiguration("attributes");
			List attrs = list.getAttributes();
			for (Iterator iter = attrs.iterator(); iter.hasNext();)
			{
				Attribute attr = (Attribute) iter.next();
				Object value = attr.getValue();
				if(value instanceof UnparsableDate)
				{
					continue;
				}
                DefaultConfiguration attrConf = toConf(attr);
				attributes.addChild(attrConf);
			}
			result.addChild(attributes);
			return result;
		}

        private DefaultConfiguration toConf(Attribute attr)
        {
            DefaultConfiguration attrConf = new DefaultConfiguration("attribute");
            String type = attr.getType().getTypeName();
            attrConf.setAttribute("type",type);
            String storedValue = attr.getStoredValue();
            attrConf.setAttribute("value", storedValue);
            return attrConf;
        }

		public List<Configuration> getMatchingConfigurations(Attribute attr) 
			throws AttributeException
		{
			log.debug("Querying lists on ["+this.dir+"] for attribute type["+attr.getType()+"] value["+attr.getValue()+"]");
			Collection files = FileUtils.listFiles(dir,extensions,false);
			List<Configuration> result = new ArrayList<Configuration>(files.size());
			for (Iterator iter = files.iterator(); iter.hasNext();)
            {
                File file = (File) iter.next();
				log.trace("Scanning file ["+file+"]");
				Configuration conf = buildConfiguration(file);
				//quick way
				if(confHoldsConfigurationFor(conf,attr))
				{
					result.add(conf);									
				}
            }
			return result;
		}

        private boolean confHoldsConfigurationFor(Configuration configuration, Attribute attr)
        {
			String typeName = attr.getType().getTypeName();
			String storedValue = attr.getStoredValue();
			Configuration[] attributes = configuration.getChild("attributes").getChildren("attribute");
			for (int i = 0; i < attributes.length; i++)
            {
                Configuration conf = attributes[i];
                String type= conf.getAttribute("type",null);
                String value = conf.getAttribute("value",null);
                if(typeName.equals(type) && storedValue.equals(value))
                {
                	return true;
                }
            }
            return false;
        }

		public Configuration getListConfiguration(long id) 
			throws AttributeException
		{
			String fileName = getFileName(id);
			String pathName = dir + File.separator + fileName;
			File file = new File(pathName);
			if(!file.exists())
			{
				throw new AttributeException("AttributeList file ["+pathName+"] not found");
			}
			Configuration conf = buildConfiguration(file);
			long listId = conf.getAttributeAsLong("id",-1);
			if(listId == id)
			{
				log.info("List id["+id+"] found on ["+file+"]");
				return conf;
			}
			throw new AttributeException("AttributeList id["+id+"]  not found on file["+pathName+"]");
		}

		private Configuration buildConfiguration(File file) 
			throws AttributeException
		{
			try
			{
				Configuration conf = builder.buildFromFile(file);
				return conf;
			}
			catch (Exception e)
			{
				throw new AttributeException("Error building configuration from file ["+file+"]",e); 		        
			}
		}

	
		String getFileName(long id)
		{
			return fileNameFormat.format(new Object[]{new Long(id)});
		}
		
		public String getFullPath(long id)
		{
			String fileName = getFileName(id);
			String fullPath = dir + File.separator+ fileName;
			return fullPath;
		}		

		public String getRepositoryName() 
		{
			return this.repoName;
		}

		public String getGroupName() 
		{
			return this.groupName;
		}

	}
}

