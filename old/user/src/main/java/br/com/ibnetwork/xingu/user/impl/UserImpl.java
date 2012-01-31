package br.com.ibnetwork.xingu.user.impl;

//XINGU
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.user.User;
import br.com.ibnetwork.xingu.user.UserManagerException;

/**
 * 
 * @author <a href="leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 */
public class UserImpl 
	extends UserSupport
{

    @Dependency
    private AttributeListManager listManager;

    private Logger log = LoggerFactory.getLogger(getClass());

    public UserImpl()
    {
		super();
    }

    public UserImpl(String username, String passwd)
    {
		super(username,passwd);
    }

    protected AttributeList retrieveAttributeList() 
    	throws UserManagerException
    {
    	log.debug("Retrieving attribute list id["+attributeListId+"] for user id["+id+"]");
        try
        {
            AttributeList result = listManager.getListById(User.class,attributeListId);
			return result;
        }
        catch (AttributeException e)
        {
			throw new UserManagerException("Error retrieveing attribute list id["+attributeListId+"] for user id["+id+"]",e);
        }
    }

	@Override
    public boolean hasAttributeList()
    {
	    return attributeListId > 0;
    }
}
