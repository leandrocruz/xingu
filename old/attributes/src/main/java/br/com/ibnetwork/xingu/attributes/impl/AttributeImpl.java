package br.com.ibnetwork.xingu.attributes.impl;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.attributes.AttributeSupport;
import br.com.ibnetwork.xingu.attributes.AttributeType;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public class AttributeImpl 
	extends AttributeSupport
{
    protected AttributeType type;
    
    protected Object value;

    public AttributeImpl()
    {
        super();
    }
    
    public AttributeImpl(String typeName, String storedValue) 
    	throws Exception
    {
		super(typeName,storedValue);
    }

    public AttributeType getType()
    {
        if(type == null)
        {
            type = typeManager.getAttributeTypeByName(typeName);
        }
        return type;
    }

    public Object getValue()
    {
        if(value == null)
        {
            value = convertStoredValue(this.storedValue);
        }
        return value;
    }

	private Object convertStoredValue(String storedValue) 
		throws AttributeException
	{
        if(storedValue == null)
        {
            return null;
        }
        
        AttributeType type = getType();
		Class clazz = type.getJavaType();
		Object result = null;
		if(clazz.equals(Date.class))
		{
			//create dates from longs
			Converter converter = ConvertUtils.lookup(Long.class);
			Long time = (Long) converter.convert(String.class,storedValue);
			result = new Date(time.longValue());
		}
		else
		{
			Converter converter = ConvertUtils.lookup(clazz);	
			result = converter.convert(String.class,storedValue);
		}
		return result;
	}

	public String toString()
	{
		return "DefaultAttribute: listId("+listId+") type("+typeName+") value("+storedValue+")";	  	
	}

}    