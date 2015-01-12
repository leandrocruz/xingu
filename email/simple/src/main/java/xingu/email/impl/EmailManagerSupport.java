package xingu.email.impl;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.email.Email;
import xingu.email.EmailManager;
import xingu.lang.Sys;
import xingu.store.ObjectStore;
import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.time.Instant;
import xingu.time.Time;
import xingu.utils.StringUtils;
import xingu.utils.TimeUtils;

public abstract class EmailManagerSupport
    implements EmailManager, Configurable, Startable, Runnable
{
    @Inject
    protected TemplateEngine templateEngine;
    
    @Inject
    protected ObjectStore store;

    @Inject
    private Time time;
    
    protected Thread worker;
    
    protected List<Email> queue = new ArrayList<Email>();
    
    protected boolean deliveryEnabled;

    private long lastSend;

    private long quota;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        this.deliveryEnabled = conf.getChild("delivery").getAttributeAsBoolean("enabled", true);
        String rate = conf.getAttribute("sendRate", "200ms"); //Required by Amazon SES. See: https://console.aws.amazon.com/ses/home
        this.quota = TimeUtils.toMillis(rate);
    }

    @Override
    public void start()
        throws Exception
    {
        worker = Sys.startDaemon(this, "EmailDispatcherThread");
    }

    @Override
    public void stop()
        throws Exception
    {
        worker.interrupt();
    }

    @Override
    public void run()
    {
        while(true)
        {
            synchronized (queue)
            {
                if(queue.size() > 0)
                {
                    Email email = queue.remove(0);
                    Instant now = time.now();
                    if(now.time() < lastSend + quota)
                    {
                    	//don't blow the send rate quota
                    	long waitFor = (lastSend + quota) - now.time();
                    	if(waitFor > quota)
                    	{
                    		logger.info("Sleeping for '{}' before sending the next message. Increase your send rate quota!", TimeUtils.toSeconds(waitFor));
                    	}
                    	try
						{
							Thread.sleep(waitFor);
						}
						catch (InterruptedException e)
						{
							logger.warn("Thread '{}' interrupted", Thread.currentThread().getName());
						}
                    }
                    try
                    {
                        boolean sent = send(email);
                        if(sent)
                        {
                        	now = time.now();
                        	lastSend = now.time();
                            Date sentDate = now.asDate();
                            email.setSent(sentDate);
                            storeMessageSent(email);
                        }
                    }
                    catch(Throwable t)
                    {
                        logger.error("Error sending email: "+email, t);
                    }
                }
                else
                {
                	try
        			{
                		queue.wait();
        			}
        			catch (InterruptedException e)
        			{
        				return;
        			}
                }
            }
        }
    }

    protected void storeMessageSent(Email email)
        throws Exception
    {
        store.store(Email.class, email);
    }
    
    @Override
    public boolean isEmailSent(String type, String to)
    {
        String namespace = Email.class.getName() + ".getSentTo";
        List<Email> emails = store.selectList(namespace, to);
        for(Email email : emails)
        {
            if(email.getType().equals(type) && email.getToAddress().equals(to))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Email findLastEmailSent(String to, String typeLike) {
        String namespace = Email.class.getName() + ".getLastEmailSentTo";
        FindEmailParameters parameters = new FindEmailParameters(to, typeLike);
        List<Email> emails = store.selectList(namespace, parameters);
        if(emails.isEmpty())
        {
            return null;
        }
        return emails.get(0);
    }

    protected abstract boolean send(Email email)
        throws Exception;

    
    protected void push(Email email)
    {
        synchronized (queue)
        {
            queue.add(email);
            queue.notify();
        }
    }

    @Override
    public void sendMessage(Email email)
        throws Exception
    {
        push(email);
    }
    
    protected String render(Email email, String template, String layout)
    {
        if(template == null)
        {
            return null;
        }
        Context context = templateEngine.createContext();
        context.put("email", email);
        StringWriter writer = new StringWriter();
        templateEngine.merge(template, context, writer);
        StringBuffer buffer = writer.getBuffer();
        String result = buffer.toString();
        
        if(StringUtils.isNotEmpty(layout))
        {
        	context.put("screen_placeholder", result);
        	writer = new StringWriter();
            templateEngine.merge(layout, context, writer);
            buffer = writer.getBuffer();
            result = buffer.toString();
        }
        
        return result;
    }

    protected Address toAddress(String name, String address)
    {
        InternetAddress result = null;
        try
        {
            result = new InternetAddress(address, name);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.info("Error creating address from "+name+" <"+address+">", e);
        }
        return result;
    }
}
