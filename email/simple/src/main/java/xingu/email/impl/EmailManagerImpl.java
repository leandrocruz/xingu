package xingu.email.impl;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import xingu.email.Email;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.messaging.MessageDispatcher;

public class EmailManagerImpl
    extends EmailManagerSupport
{
    @Inject
    private MessageDispatcher messageDispatcher;

    protected boolean send(Email email)
        throws Exception
    {
        if (!deliveryEnabled)
        {
            logger.warn("Email delivery is not enabled!");
            return false;
        }

        try
        {
            Message message = createMessageFrom(email);
            return messageDispatcher.sendMessage(message);
        }
        catch (Throwable t)
        {
            logger.error("Error sending message ", t);
            return false;
        }
    }

    protected Message createMessageFrom(Email email)
        throws Exception
    {
        Address to = toAddress(email.getToName(), email.getToAddress());
        Address from = toAddress(email.getFromName(), email.getFromAddress());
        String subject = email.getSubject();
        Message message = new MimeMessage(messageDispatcher.getSession());
        message.setFrom(from);
        message.setRecipient(RecipientType.TO, to);
        message.setSubject(MimeUtility.encodeText(subject));

        String html = render(email, email.getHtmlTemplate(), email.getHtmlLayoutTemplate());
        
        //TODO: use multipart and add the text content 
        String txt = render(email, email.getTextTemplate(), email.getTextLayoutTemplate());
        
        message.setContent(html, "text/html; charset=utf-8");
        return message;
    }
}