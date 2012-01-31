package br.com.ibnetwork.xingu.messaging;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.utils.FSUtils;

@Ignore
public class EmailMessageDispatcherTest 
	extends XinguTestCase
{
    private String SUBJECT = "Xingu Messaging [áéíóú]";

    //private EventManager eventManager;

    private String ENCODING = "UTF-8";

    @Inject
    private MessageDispatcher md;

    public EmailMessageDispatcherTest()
    {
        super();
    }
    
    @Test
    public void testSendJavaxMessage()
    	throws Exception
    {
    	Session session = md.getSession();
		MimeMessage message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress("email@admin"));
		message.addRecipients(javax.mail.Message.RecipientType.TO, "email@admin");
		message.setSubject("Teste multipart alternative com anexos");
		
		MimeMultipart emailBody = new MimeMultipart();
		
		MimeMultipart readeable = new MimeMultipart("alternative");
		
		// text alternative
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("Text message", "text/plain");
		readeable.addBodyPart(textPart);
		
		 // html alternative
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent("<html><body><h1>html message</h1></body></html>", "text/html");
		readeable.addBodyPart(htmlPart);
		
		MimeBodyPart readeablePart = new MimeBodyPart();
		readeablePart.setContent(readeable);		
		emailBody.addBodyPart(readeablePart);
		
		//adicionando anexos
		
		//anexos
		File f1 = FSUtils.loadAsFile("attachments/mascote.jpg");
		File f2 = FSUtils.loadAsFile("attachments/Page.dfSequence.wmf");
		File f3 = FSUtils.loadAsFile("attachments/sortTable.zip");
		
		MimeMultipart attachs = new MimeMultipart();
		
		//adicionando anexo 1
		BodyPart attach1 = new MimeBodyPart();
		DataSource source = new FileDataSource(f1.getAbsoluteFile());
		attach1.setDataHandler(new DataHandler(source));
		attach1.setFileName(f1.getName());
		attach1.setDisposition(BodyPart.ATTACHMENT);
		attachs.addBodyPart(attach1);

		//adicionando anexo 2
		BodyPart attach2 = new MimeBodyPart();
		source = new FileDataSource(f2.getAbsoluteFile());
		attach2.setDataHandler(new DataHandler(source));
		attach2.setFileName(f2.getName());
		attach2.setDisposition(BodyPart.ATTACHMENT);
		attachs.addBodyPart(attach2);
		
		//adicionando anexo 3
		BodyPart attach3 = new MimeBodyPart();
		source = new FileDataSource(f3.getAbsoluteFile());
		attach3.setDataHandler(new DataHandler(source));
		attach3.setFileName(f3.getName());
		attach3.setDisposition(BodyPart.ATTACHMENT);
		attachs.addBodyPart(attach3);
		
		MimeBodyPart attachsPart = new MimeBodyPart();
		attachsPart.setContent(attachs);
		emailBody.addBodyPart(attachsPart);
		
		emailBody.setSubType("mixed");
		message.setContent(emailBody);
		
		md.sendMessage(message);
    }

}
/*
class SimpleObserver
	implements Observer
{

    public Object source;

    public boolean updateCalled;

    public void update(Event event)
    {
		this.source = event.getSource();
		updateCalled = true;
    }
}
*/	