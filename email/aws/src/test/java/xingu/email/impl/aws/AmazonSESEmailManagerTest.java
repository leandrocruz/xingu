package xingu.email.impl.aws;

import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.email.EmailManager;
import xingu.email.SimpleEmail;

public class AmazonSESEmailManagerTest
    extends XinguTestCase
{
    @Inject
    private EmailManager mailer;

    @Override
    protected void rebind(Binder binder)
    {
        binder.bind(EmailManager.class).to(AmazonSESEmailManager.class);
    }

    @Ignore
    @Test
    public void testSendMessage()
        throws Exception
    {
        SimpleEmail e = new SimpleEmail();
        e.setFrom("Kidux", "cadastro@kidux.com.br");
        e.setTo("Jonas Pacheco", "jonaspp@gmail.com");
        e.setSubject("Email núi didi");
        e.setHtmlTemplate("TestHtml");
        e.setTextTemplate("TestTxt");
        mailer.sendMessage(e);
    }
}