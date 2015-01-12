package xingu.email.impl;

import xingu.email.Email;
import xingu.email.EmailManager;
import xingu.lang.NotImplementedYet;

public class EmailManagerImpl
	implements EmailManager
{

	@Override
	public void sendMessage(Email email)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isEmailSent(String type, String to)
	{
		throw new NotImplementedYet();
	}

	@Override
	public Email findLastEmailSent(String to, String typeLike)
	{
		throw new NotImplementedYet();
	}
}
