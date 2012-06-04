package xingu.url;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.ibnetwork.xingu.lang.BadParameter;

public class UrlValidationTest
{
    @Test
    public void testValidUrls()
    {
        assertTrue(isValid("kidux.net"));
        assertTrue(isValid("www.kidux.net"));
        assertTrue(isValid("http://a.b"));
        assertTrue(isValid("http://a.b.c"));
        assertTrue(isValid("http://kidux.net"));
        assertTrue(isValid("http://www.kidux.net"));
        assertTrue(isValid("http://www.kidux.net:80"));
        assertTrue(isValid("http://www.kidux.net:80/"));
        assertTrue(isValid("http://www.kidux.net:80?"));
        assertTrue(isValid("http://www.kidux.net:80#"));
        assertTrue(isValid("http://www.kidux.net:80/?"));
        assertTrue(isValid("http://www.kidux.net:80/#"));
        assertTrue(isValid("http://www.kidux.net:80/?#"));
        assertTrue(isValid("http://www.kidux.net:80/search"));
        assertTrue(isValid("http://www.kidux.net:80/search#"));
        assertTrue(isValid("http://www.kidux.net:80/search#abc"));
        assertTrue(isValid("http://www.kidux.net:80/search?"));
        assertTrue(isValid("http://www.kidux.net:80/search?q="));
        assertTrue(isValid("http://www.kidux.net:80/search?q=abc"));
        assertTrue(isValid("http://www.kidux.net:80/search?q=abc&z=123"));
        assertTrue(isValid("http://www.kidux.net:80/search?query=abc123_%20pa&zig=123"));
        assertTrue(isValid("http://www.kidux.net:80/?query=abc123_%20pa&zig=123"));
        assertTrue(isValid("http://www.kidux.net:80?query=abc123_%20pa&zig=123"));
        assertTrue(isValid("http://localhost"));
        assertTrue(isValid("http://localhost/"));
        assertFalse(isValid("http://jaspion:8887/kastle"));
        assertFalse(isValid("http://some_123_machine_name-9/"));
    }
    
    @Test
    public void testMalformedUrl1()
    {
        assertFalse(isValid("http:/www.kidux.net"));
    }
    
    @Test(expected=BadParameter.class)
    public void testMalformedUrl2()
    {
        assertFalse(isValid("http:www.kidux.net"));
    }
    
    @Test
    public void testMalformedUrl3()
    {
        assertFalse(isValid("http://:80"));
    }

    @Test
    public void testMalformedUrl4()
    {
        assertFalse(isValid("http://www.music.com:"));
    }
    
    @Test
    public void testMalformedUrl5()
    {
        assertFalse(isValid("http://www.cartoons.br:/"));
    }
    
    @Test(expected=BadParameter.class)
    public void testMalformedUrl6()
    {
        assertFalse(isValid("http://some invalid hostname"));
    }
    
    @Test(expected=BadParameter.class)
    public void testMalformedUrl7()
    {
        assertFalse(isValid("http://www:movies.org"));
    }
    
    private boolean isValid(String urlSpec)
    {
        Url url = UrlParser.parse(urlSpec);
        return url.isValid();
    }
}
