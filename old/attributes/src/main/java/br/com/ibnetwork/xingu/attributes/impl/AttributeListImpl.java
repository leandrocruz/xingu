package br.com.ibnetwork.xingu.attributes.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.attributes.Attribute;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.attributes.AttributeType;
import br.com.ibnetwork.xingu.attributes.AttributeTypeManager;
import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.container.Dependency;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 * Default implementation of AttributeList using a ArrayList backend
 */
public class AttributeListImpl
	implements AttributeList
{
    private long id;
        
    private String nameSpace;
    
	@Dependency
    private AttributeTypeManager typeManager;
	
    @Dependency
	private AttributeListManager listManager;

	private List<Attribute> attributes;

    public AttributeListImpl(ArrayList<Attribute> attributes, Long id)
    {
        this(attributes,id.longValue());
    }
    
    public AttributeListImpl(ArrayList<Attribute> attributes, long id)
    {
        this.id = id;
        this.attributes = attributes;
    }
    
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
        List<Attribute> attributes = getAttributes();
        for (Attribute attribute : attributes)
        {   
            attribute.setListId(id);
        }
    }
    
    public void setListManager(AttributeListManager listManager)
    {
        this.listManager = listManager;
    }
    
	public String getNameSpace()
    {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace)
    {
        this.nameSpace = nameSpace;
    }

    public int size() {
		return getAttributes().size();
	}


    public boolean contains(AttributeType type)
    {
		for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();)
    	{
        	Attribute attr = iter.next();
        	if(attr.getType().equals(type))
        	{
        		return true;
        	}
    	}
        return false;
    }

    public List<Attribute> getAttributes()
    {
        return attributes;
    }
	
	public String toString()
	{
		return attributes.toString();
	}

    /**
     * M?todo que ir? varrer a lista de atributos e verificar se h? o atributo na lista
     * caso houver, o atrributo ser? retornado, caso n?o houver ser? retornado null
     * 
     * @param type tipo do atributo que ser? procurado
     * @return Attribute attributo que cont?m o tipo indicado no param
     */
    public Attribute get(AttributeType type)
    {
		for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();)
		{
			Attribute element = iter.next();
			AttributeType elementType = element.getType();
			if(elementType.equals(type))
			{
				return element;            	
			}
		}
		return null;
    }

	/**
	  * M?todo que ir? varrer a lista de atributos e verificar se h? algum atributo na lista igual ao parametro.
	  * Caso houver, ser? retornado true, caso n?o houver ser? retornado false
	  * 
	  * @param attr atributo que ser? procurado
	  * @return boolean true se houver atributo igual ao par?metro, false caso contrario. 
	  */
    public boolean contains(Attribute attr)
    {
    	if(attr == null)
    	{
    		return false;
    	}
    	Attribute sameType = get(attr.getType());
		return sameType == null ? false : sameType.equals(attr);
    }

    public boolean isSuperSet(AttributeList otherAttrList)
    {
    	if(otherAttrList == null)
    	{
    		return false;
    	}
    	
    	if(this == otherAttrList)
    	{
    		return true;
    	}
    	
    	for (Iterator iter = otherAttrList.getAttributes().iterator(); iter.hasNext();)
        {
            Attribute element = (Attribute)iter.next();
			AttributeType elementType = element.getType();
			if(!contains(elementType))
			{
				return false;
			}
        }
    	
        return true;
    }

	public Map toMap()
	{
		Map<String, Object> map = new HashMap<String, Object>();
        for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();)
		{
			Attribute element = iter.next();
			map.put(element.getType().getTypeName(), element.getValue());
		}
		return map;
	}

    public Attribute get(String typeName) 
    	throws AttributeException
    {
		AttributeType type = typeManager.getAttributeTypeByName(typeName);
		Attribute attr = get(type);
		return attr;
    }

    public void delete(Attribute attr) 
    	throws AttributeException
    {
        attributes.remove(attr);
    }

    public void put(Attribute attr) 
    	throws AttributeException
    {
		if(attr == null)
		{
			throw new IllegalArgumentException("Can't add null attribute to list");	
		}
        attr.setListId(this.id);
		Attribute old = get(attr.getType());
		if(old != null)
		{
			int index = attributes.indexOf(old);
			attributes.set(index,attr);
		}
		else
		{
			attributes.add(attr);
		}
    }

	public void put(String name, Object value) 
		throws AttributeException 
	{
	    Attribute attr = listManager.create(name, value);
		put(attr);
	}

}
