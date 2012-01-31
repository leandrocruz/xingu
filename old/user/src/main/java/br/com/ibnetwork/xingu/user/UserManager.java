package br.com.ibnetwork.xingu.user;

//JDK
import java.util.List;

public interface UserManager
{
    public String ROLE = UserManager.class.getName();
    
    public User getById(long id)
        throws UserManagerException;

    public User getByUsername(String username)
        throws UserManagerException;

    public List<User> getAll()
        throws UserManagerException;

    public void store(User user)
        throws UserManagerException;

    public void delete(User user)
        throws UserManagerException;
}
