package xingu.email.impl.chimp;

import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.email.EmailManager;
import xingu.email.SimpleEmail;

public class MailChimpEmailManagerTest
    extends XinguTestCase
{
    @Inject
    private EmailManager mailer;

    @Override
    protected void rebind(Binder binder)
    {
        binder.bind(EmailManager.class).to(MailChimpEmailManager.class);
    }

    @Ignore
    @Test
    public void testSendMessage()
        throws Exception
    {
        SimpleEmail email = new SimpleEmail();
        email.setFrom("Kidux", "cadastro@kidux.com.br");
        email.setTo("Leandro", "leandro.saad@gmail.com");
        email.setSubject("MailChimp STS test");
        email.setHtmlTemplate("TestHtml");
        email.setTextTemplate("TestTxt");
        mailer.sendMessage(email);
    }
}