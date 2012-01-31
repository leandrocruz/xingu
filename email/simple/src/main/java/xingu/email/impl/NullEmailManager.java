package xingu.email.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.email.Email;
import xingu.email.EmailManager;

public class NullEmailManager
    implements EmailManager
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendMessage(Email email) 
        throws Exception
    {
        logger.info("Email sent to " + email.getToAddress() + ", subject(" + email.getSubject() + "), type(" + email.getType() + ")");
    }

    @Override
    public boolean isEmailSent(String emailCode, String to) 
    {
        return false;
    }
}
