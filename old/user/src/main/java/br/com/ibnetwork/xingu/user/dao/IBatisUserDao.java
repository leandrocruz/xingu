package br.com.ibnetwork.xingu.user.dao;

import br.com.ibnetwork.xingu.dao.impl.ibatis.IbatisDaoSupport;
import br.com.ibnetwork.xingu.user.User;

public class IBatisUserDao
	extends IbatisDaoSupport<Long, User>
    implements UserDao
{

	@Override
    protected String getNameSpace()
    {
	    return "user";
    }

	public User getByUsername(String username)
    {
	    return queryObject("getByUsername", username);
    }
}
