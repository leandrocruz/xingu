package xingu.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class FileUrlTest
{
    private Url url;
    
    @Before
    public void createUrl()
    {
        url = UrlParser.parse("file:///C:/Documents%20and%20Settings/John Doe/Desktop/page.html");
    }
    
    @Test
    public void testScheme()
    {
        assertEquals("file", url.getScheme());
    }
    
    @Test
    public void testHost()
    {
        assertEquals("localhost", url.getHost());
    }
    
    @Test
    public void testDomainName()
    {
        assertNull(url.getDomainName());
    }
    
    @Test
    public void testPath()
    {
        assertEquals("/C:/Documents%20and%20Settings/John Doe/Desktop/page.html", url.getPath());
    }
}