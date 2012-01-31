package br.com.ibnetwork.xingu.user.dao;

import br.com.ibnetwork.xingu.dao.Dao;
import br.com.ibnetwork.xingu.user.User;

public interface UserDao
	extends Dao<Long, User>
{
	User getByUsername(String username);
}
