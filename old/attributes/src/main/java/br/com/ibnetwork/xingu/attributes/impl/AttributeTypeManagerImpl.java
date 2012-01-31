package br.com.ibnetwork.xingu.attributes.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import br.com.ibnetwork.xingu.attributes.AttributeType;
import br.com.ibnetwork.xingu.attributes.AttributeTypeManager;
import br.com.ibnetwork.xingu.attributes.AttributeTypeManagerException;
import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.factory.FactoryException;

public class AttributeTypeManagerImpl
	implements AttributeTypeManager, Disposable, Configurable
{
    @Dependency
	private Factory factory;
	
	private static final String ID_TYPE_NAME = "ID";

	private Map<String,AttributeType> attributeMap;
	
    private static Map<String,Class> classMap;
    
    static 
    {
        classMap = new HashMap<String,Class>();
        classMap.put("Boolean",Boolean.class);
        classMap.put("Character",Character.class);
        classMap.put("String",String.class);
        classMap.put("Date",Date.class);
        classMap.put("Integer",Integer.class);
        classMap.put("Long",Long.class);
        classMap.put("Double",Double.class);
    }

	public AttributeType getAttributeTypeByName(String name)
		throws AttributeTypeManagerException
	{
		name = name.toUpperCase();
		AttributeType type = attributeMap.get(name);
		if(type == null)
		{
			type = createType(name, "String");
			attributeMap.put(name,type);
		}
		return type;
	}

	public Collection getAllTypes() 
		throws AttributeTypeManagerException
	{
		return attributeMap.values();
	}

	public void dispose()
	{
		factory = null;
	}

	public void configure(Configuration conf) 
		throws ConfigurationException
	{
		Configuration attributeTypeConf[] = conf.getChild("attributeTypes").getChildren();
		attributeMap = new HashMap<String,AttributeType>(attributeTypeConf.length);
		try
		{
			for (int i = 0; i < attributeTypeConf.length; i++)
			{
				String name = attributeTypeConf[i].getAttribute("name");
				String javaType = attributeTypeConf[i].getAttribute("type");
				AttributeType type = createType(name, javaType);
				attributeMap.put(name,type);
			}
		}
		catch (FactoryException e)
		{
			throw new ConfigurationException("Error creating AttributeType",e);
		}
	}

	private AttributeType createType(String name, String javaType) 
	{
		name = name.toUpperCase();
		Class javaClass = classMap.get(javaType);
		Object[] params = new Object[]{name,javaClass};
		AttributeType attributeType = (AttributeType) factory.create(AttributeType.class,params);
		return attributeType;
	}

    public AttributeType getIdType() 
    	throws AttributeTypeManagerException
    {
		return getAttributeTypeByName(ID_TYPE_NAME);
    }
}
