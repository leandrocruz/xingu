package br.com.ibnetwork.xingu.user;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class UserManagerException 
    extends NestableRuntimeException
{

    private String username;

    private long id;

    public UserManagerException(String message, Exception e)
    {
        super(message,e);
    }

    public UserManagerException()
    {
        super();
    }

    public UserManagerException(String message)
    {
        super(message);
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setUserId(long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public long getUserId()
    {
        return id;
    }
}
