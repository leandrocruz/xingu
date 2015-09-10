package br.com.ibnetwork.xingu.messaging.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.messaging.MessageDispatcher;
import br.com.ibnetwork.xingu.messaging.MessageDispatcherException;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">Leandro Rodrigo Saad Cruz</a>
 *
 */
public class MessageDispatcherImpl 
	implements MessageDispatcher, Configurable
{
    private Properties props;

    private Session session;
	
    private Logger logger = LoggerFactory.getLogger(getClass());
	
    public void configure(Configuration configuration) 
    	throws ConfigurationException
    {
        Configuration conf = configuration.getChild("smtpServer"); 
        String smtpServer = conf.getAttribute("address","localhost");
        String smtpPort = conf.getAttribute("port","25");
		boolean debug = conf.getAttributeAsBoolean("debug", false);
		boolean authEnabled = conf.getAttributeAsBoolean("authEnabled", false);
		boolean tlsEnabled = conf.getAttributeAsBoolean("tlsEnabled", false);
		props = new Properties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.debug", new Boolean(debug).toString());
		props.put("mail.smtp.auth", new Boolean(authEnabled).toString());
		if (tlsEnabled)
		{
		    props.put("mail.smtp.starttls.enable", Boolean.toString(true));
		}
		Authenticator auth = null;
		if(authEnabled)
		{
			String username = conf.getAttribute("username");
			String password = conf.getAttribute("password");
			auth = new SMTPAuthenticator(username,password);	
		}
		session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug); 
    }

    public Session getSession() 
        throws MessageDispatcherException 
    {
        return this.session;
    }

    public boolean sendMessage(Message message) 
		throws MessageDispatcherException 
	{		
		try 
		{
			Transport.send(message);
			return true;
		} 
		catch (Exception e) 
		{
			logger.error("Error sending email message",e);
		}		
		return false;
	}
}

class SMTPAuthenticator 
	extends javax.mail.Authenticator 
{
	private String user;
	
	private String password;

	public SMTPAuthenticator(String user, String password) 
	{
		this.user = user;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() 
	{
		return new PasswordAuthentication(user, password);
	}
}