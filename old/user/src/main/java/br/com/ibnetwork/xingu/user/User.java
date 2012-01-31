package br.com.ibnetwork.xingu.user;

import java.util.Date;

import br.com.ibnetwork.xingu.attributes.AttributeList;
import xingu.store.PersistentBean;

/**
 * Interface que define um usuario no sistema
 * 
 * @author <a href="leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
  */
public interface User
	extends PersistentBean
{
    String getUsername();
    void setUsername(String username);
    
    String getPassword();
    void setPassword(String password);
    
    Date getRegistrationDate();
    void setRegistrationDate(Date date);
    
    boolean isActive();
    void setActive(boolean active);
    
    boolean isConfirmed();
    void setConfirmed(boolean value);
    
    boolean isComplete();
    void setComplete(boolean value);
    
    String getConfirmationCode();
    void setConfirmationCode(String code);
    
    String getName();
    void setName(String name);
    
    /* attributes */
    
    long getAttributeListId();
	void setAttributeListId(long id);
    
    boolean hasAttributeList();
    
    AttributeList getAttributeList()
		throws UserManagerException;
	
    void setAttributeList(AttributeList attrList);
    
}
