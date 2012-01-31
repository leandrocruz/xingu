package br.com.ibnetwork.xingu.attributes;

import br.com.ibnetwork.xingu.container.Dependency;

public abstract class AttributeSupport 
	implements Attribute
{
    protected long id;
	
	protected long listId;
	
    protected String typeName;

    protected String storedValue;
	
	@Dependency
    protected AttributeTypeManager typeManager;
    
    public AttributeSupport() 
    {}

    public AttributeSupport(String typeName, String storedValue)
    {
        this.typeName = typeName;
        this.storedValue = storedValue;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getListId()
    {
        return listId;
    }

    public void setListId(long listId)
    {
        this.listId = listId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public void setStoredValue(String value)
    {
        this.storedValue = value;
    }
    
	public String getStoredValue()
	{
		return storedValue;
	}
    
	public boolean equals(Object other)
	{
		if(other == this) return true;
		if(!(other instanceof Attribute))
		{
			return false; 
		}
		Attribute otherAttr = (Attribute) other;
		boolean valueEquals = storedValue.equals(otherAttr.getStoredValue());
		boolean typeEquals = typeName.equals(otherAttr.getType().getTypeName());  
		return valueEquals && typeEquals; 
	}
}
