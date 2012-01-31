package br.com.ibnetwork.xingu.user.impl;

//XINGU
import java.util.Date;

import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.user.User;
import br.com.ibnetwork.xingu.user.UserManagerException;

/**
 * @author <a href="leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public abstract class UserSupport 
    implements User
{
    protected long id;
    
    protected String name;
    
    protected String username;
    
    protected String password;
    
    protected Date registrationDate;
    
    protected boolean confirmed;
    
    protected boolean complete;
    
    protected String confirmationCode;

	protected long attributeListId = -1;
    
    protected boolean active;
	
	protected AttributeList attributeList;

    public UserSupport()
    {
        //don't remove
    }

    public UserSupport(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public String getPassword() {return password;}
	public String getConfirmationCode(){return confirmationCode;}
    public void setConfirmationCode(String confirmationCode){this.confirmationCode = confirmationCode;}
    public boolean isConfirmed(){return confirmed;}
    public void setConfirmed(boolean confirmed){this.confirmed = confirmed;}
    public boolean isComplete(){return complete;}
    public void setComplete(boolean value){this.complete = value;}
	public Date getRegistrationDate(){return registrationDate;}
    public void setRegistrationDate(Date registrationDate){this.registrationDate = registrationDate;}
    public boolean isActive(){return active;}
    public void setActive(boolean active){this.active = active;}
    public long getAttributeListId(){return this.attributeListId;}
    public void setAttributeListId(long id){this.attributeListId = id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

	public String getDisplay()
    {
	    return username;
    }

	public AttributeList getAttributeList() 
    	throws UserManagerException
    {
        if(hasAttributeList() && attributeList == null)
        {
			attributeList = retrieveAttributeList();
        }
        return attributeList;
    }

	public void setAttributeList(AttributeList attrList)
	{
		this.attributeListId = attrList.getId();
		this.attributeList = attrList;
	}

	protected abstract AttributeList retrieveAttributeList()
		throws UserManagerException;

	public abstract boolean hasAttributeList();
	
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof User))
		{
			return false;
		}
		long objId = ((User)obj).getId();
		if (this.id == objId)
		{
			return true;
		}
		return false;
	}
}
