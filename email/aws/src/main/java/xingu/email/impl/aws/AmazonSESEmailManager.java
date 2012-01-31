package xingu.email.impl.aws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.email.Email;
import xingu.email.impl.EmailManagerSupport;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

public class AmazonSESEmailManager
    extends EmailManagerSupport
    implements Configurable
{    
    private PropertiesCredentials credentials;
    
    private String bounceIfMissingAddress;
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
    	super.configure(conf);
        try
        {
            Configuration addresses = conf.getChild("addresses");
            Configuration cred = conf.getChild("credentials");
            bounceIfMissingAddress = addresses.getAttribute("bounce");
            String keyId = cred.getAttribute("keyId", "null");
            String secret = cred.getAttribute("secret", "null");
            String streamContent = "accessKey=" + keyId + "\nsecretKey=" + secret;
            InputStream is = new ByteArrayInputStream(streamContent.getBytes("UTF8"));
            credentials = new PropertiesCredentials(is);
        }
        catch (Exception e)
        {
            credentials = null;
            logger.error("Could not read credentials from pulga", e);
        }
    }
    
    @Override
    protected boolean send(Email e)
        throws Exception
    {
        if(credentials == null)
        {
            throw new NotImplementedYet();
        }               
        
        Content subject = new Content();
        subject.withData(e.getSubject());
        
        AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);
        Destination destination = new Destination().withToAddresses(e.getToAddress());
        
        Body body = new Body();

        String htmlTemplate = render(e, e.getHtmlTemplate(), e.getHtmlLayoutTemplate());
        if(htmlTemplate != null)            
        {
        	Content htmlContent = new Content(htmlTemplate).withCharset("UTF-8");
        	body.withHtml(htmlContent);
        }
            
        String txtTemplate = render(e, e.getTextTemplate(), e.getTextLayoutTemplate());
        if(txtTemplate != null)
        {
        	Content txtContent = new Content(txtTemplate).withCharset("UTF-8");
        	body.withText(txtContent);
        }
        
        Message msg = new Message(subject, body);
        String from = e.getFromName() + "<" + e.getFromAddress() + ">";
        SendEmailRequest request = new SendEmailRequest(from, destination, msg);
        String bounceTo = e.getBounceTo();
        bounceTo = bounceTo != null ? bounceTo : bounceIfMissingAddress;
        request.setReturnPath(bounceTo);

        try
        {
            SendEmailResult result = service.sendEmail(request);
            logger.info("Email sent to " + e.getToAddress() + ", messageId(" + result.getMessageId() + ")");
            return true;
        }
        catch(AmazonServiceException ex)
        {
            logger.warn("Failed sending email to " + e.getToAddress());
            logger.error("Request id: " + ex.getRequestId() + 
                         " - Status code: " + ex.getStatusCode() + 
                         " - Error code: " + ex.getErrorCode() + 
                         " - Error message: " + ex.getLocalizedMessage());
            return false;
        }
    }
}