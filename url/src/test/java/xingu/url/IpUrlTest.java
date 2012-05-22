package xingu.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IpUrlTest
{
    private static final String IP = "74.207.227.68";

    private static final String URL_SPEC = "http://"+IP+":1234/path/to/something";
    
    private Url url;
    
    @Before
    public void setUp()
    {
        url = UrlParser.parse(URL_SPEC);
    }
    
    @Test
    public void testHost()
    {
        assertEquals(IP, url.getHost());
    }
    
    @Test
    public void testDomainName()
    {
        assertNull(url.getDomainName());
    }
    
    @Test
    public void testIsValid()
    {
        assertTrue(url.isValid());
    }
}
