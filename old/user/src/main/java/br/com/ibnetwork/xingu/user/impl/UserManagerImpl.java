package br.com.ibnetwork.xingu.user.impl;

//XINGU
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.attributes.AttributeListManager;
import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.dao.DaoManager;
import xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.user.User;
import br.com.ibnetwork.xingu.user.UserManager;
import br.com.ibnetwork.xingu.user.UserManagerException;
import br.com.ibnetwork.xingu.user.dao.UserDao;

public class UserManagerImpl
    implements UserManager
{
    private Logger logger = LoggerFactory.getLogger(getClass());
 
    @Dependency
    private ObjectStore store;
    
    @Dependency
    private DaoManager daoManager;
    
    @Dependency
    private AttributeListManager listManager;
    

    public User getById(long id)
        throws UserManagerException
    {
    	return store.getById(User.class, id);
    }

	public User getByUsername(String username)
        throws UserManagerException
    {
		UserDao dao = daoManager.getDao(User.class); 
	    return dao.getByUsername(username);
    }
    
    public void store(User user)
        throws UserManagerException
    {
        try
		{
            store.store(user);
        	AttributeList list = user.getAttributeList();
            if(list != null)
            {
                listManager.store(User.class, list);
            }
			logger.info("user [" + user.getUsername() + "] added");
		}
		catch (Exception e)
		{
			UserManagerException ex = new UserManagerException("Error adding user", e);
			ex.setUsername(user.getUsername());
			throw ex;
		}
    }

    public void delete(User user)
        throws UserManagerException
    {
        try
        {
            if(user.hasAttributeList())
            {
                listManager.remove(User.class,user.getAttributeListId());
            }
            store.delete(user);
        }
        catch (Exception e)
        {
            throw new UserManagerException("Error removing user id["+user.getId()+"]", e);
        }
    }

    public List<User> getAll()
        throws UserManagerException
    {
    	return store.getAll(User.class);
    }
}
