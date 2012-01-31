package br.com.ibnetwork.xingu.attributes.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.attributes.Attribute;
import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.attributes.AttributeType;
import br.com.ibnetwork.xingu.attributes.AttributeTypeManager;
import br.com.ibnetwork.xingu.attributes.UnparsableDate;
import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.factory.FactoryException;
import br.com.ibnetwork.xingu.idgenerator.Generator;
import br.com.ibnetwork.xingu.idgenerator.GeneratorException;
import br.com.ibnetwork.xingu.idgenerator.IdGenerator;

public abstract class AttributeListManagerSupport 
	implements AttributeListManager, Initializable
{
	protected long defaultId = -1;
	
	protected AttributeType idType;

    protected String generatorId = "attribute-list";
	
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Inject
    private Container container;

    @Inject
	private IdGenerator idGenerator;
	
    @Inject
    protected Factory factory;
	
    @Inject
    protected AttributeTypeManager typeManager;

    private String[] SIGNATURE = new String[]{String.class.getName(),String.class.getName()};

	public void initialize() 
		throws Exception 
	{
	    idType = typeManager.getIdType();
	}
    
    protected String getNameSpace(String group, String repo)
    {
        return group + "." + repo;
    }

    protected long setListId(AttributeList list)
    {
        long id;
        try
        {
            Generator<Long> generator = (Generator<Long>) idGenerator.generator(generatorId);
            Long objectId = generator.next();
            id = objectId.longValue();
            list.setId(id);
            return id;
        }
        catch (GeneratorException e)
        {
            throw new AttributeException("Error creating new list id",e);
        }
    }

    protected String convertFromValue(Object value) 
	    throws AttributeException
	{
	    if(value instanceof UnparsableDate)
	    {
	        return null;
	    }
	    if(value instanceof Date)
	    {
	        long time = ((Date)value).getTime();
	        return String.valueOf(time);
	    }
	    else
	    {
	        return value.toString();
	    }
	}
    
    public AttributeList create(long id, List<Attribute> attributes)
        throws AttributeException
    {
        long listId = defaultId;
        if(id > 0)
        {
            listId = id; 
        }
        Object[] params = new Object[]{attributes,new Long(listId)};
        AttributeList list = (AttributeList) factory.create(AttributeList.class,params); 
        //HACK: inject depencies
        list.setListManager(this);
        try
        {
            for (Attribute attribute : attributes)
            {
                container.startLifecycle(attribute, null);
            }
        }
        catch(Exception e)
        {
            throw new AttributeException("Error handling attribute lifecycle",e);
        }

        return list;
    }

    public Attribute create(String typeName, Object value) 
        throws AttributeException
    {
        String storedValue = null;
        if(value instanceof String)
        {
            storedValue = (String) value;   
        }
        else
        {
            storedValue= convertFromValue(value);
        }
        storedValue = StringUtils.trimToNull(storedValue);
        Object[] params = new Object[]{typeName, storedValue};
        Attribute attr= (Attribute) factory.create(Attribute.class,params, SIGNATURE);
        return attr;
    }

    public AttributeList create(Map map)
        throws AttributeException
    {
        List<Attribute> result = new ArrayList<Attribute>();
        for (Iterator<?> iter = map.keySet().iterator(); iter.hasNext();)
        {
            String key = (String) iter.next();
            try
            {
                AttributeType type = typeManager.getAttributeTypeByName(key);
                Object value = map.get(key);
                if(type == null)
                {
                    log.debug("Ignoring attribute type["+type+"] value["+value+"]");
                    continue;
                }
                Attribute attribute = create(type.getTypeName(),value);
                result.add(attribute);
            }
            catch (Exception e)
            {
                throw new  FactoryException("Error creating Attribute using key ["+key+"]",e);
            }
        }
        Object id = map.get(idType.getTypeName());
        Long listId = new Long(defaultId);
        if(id != null && id instanceof Long)
        {
            listId = (Long) id;
        }
        AttributeList attrList = create(listId, result);
        return attrList;
    }

    /* getById */
    public AttributeList getListById(long id) 
        throws AttributeException
    {
    	return getListById(AttributeListManager.DEFAULT_REPO_NAME, id);
    }

    public AttributeList getListById(Class clazz, long id) 
        throws AttributeException
    {
        return getListById(clazz.getName(),id);
    }
    
    public AttributeList getListById(String repoName, long id) 
        throws AttributeException
    {
    	return getListById(DEFAULT_GROUP_NAME,repoName,id);
    }

    /* store */
    public long store(AttributeList list) 
        throws AttributeException
    {
        return store(DEFAULT_GROUP_NAME, DEFAULT_REPO_NAME, list);
    }

    public long store(Class clazz, AttributeList list) 
        throws AttributeException
    {
        return store(clazz.getName(),list);
    }

    public long store(String repoName, AttributeList list) 
        throws AttributeException
    {
        return store(DEFAULT_GROUP_NAME,repoName,list);
    }

    /* remove */
    public void remove(long id) 
        throws AttributeException
    {
    	remove(DEFAULT_GROUP_NAME, DEFAULT_REPO_NAME, id);
    }

    public void remove(Class clazz, long id) 
        throws AttributeException
    {
        remove(clazz.getName(), id);
    }

    public void remove(String repoName, long id) 
        throws AttributeException
    {
    	remove(DEFAULT_GROUP_NAME, repoName, id);
    }
}
