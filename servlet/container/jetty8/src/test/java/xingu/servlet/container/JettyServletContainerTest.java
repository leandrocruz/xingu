package xingu.servlet.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;

@Ignore
public class JettyServletContainerTest
    extends XinguTestCase
{
    @Inject
    private ServletContainer container;
    
    
    @Override
    protected String getContainerFile()
    {
        return "pulga-servlet.xml";
    }

    @Test
    public void testContextRestart()
        throws Exception
    {
        ApplicationContext ctx = container.context("/test");
        assertTrue(ctx.isRunning());

        URL url = new URL("http://localhost:9192/test");
        assertEquals("Test.Index", read(url));
        
        ctx.stop();
        assertFalse(ctx.isRunning());
        try
        {
            read(url);
            fail("Should have thrown exception");
        }
        catch(FileNotFoundException e)
        {}
        
        ctx.start();
        assertTrue(ctx.isRunning());
        assertEquals("Test.Index", read(url));
    }

    public String read(URL url)
        throws Exception
    {
        StringBuffer sb = new StringBuffer();
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
        {
            sb.append(inputLine);
        }
        in.close();
        return sb.toString();
    }
}