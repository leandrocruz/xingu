package xingu.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

import xingu.url.Url;
import xingu.url.UrlParser;

public class UrlUtilsTest
{
    @Test
    public void testRegex()
    {
        Matcher m = UrlUtils.pattern.matcher("http://a");
        assertTrue(m.matches());
        assertEquals("http", m.group(1));

        m = UrlUtils.pattern.matcher("https://a");
        assertTrue(m.matches());
        assertEquals("https", m.group(1));
        
        m = UrlUtils.pattern.matcher("a");
        assertTrue(m.matches());
        assertEquals(null, m.group(1));

    }
    
    @Test
    public void testIsSameDomain()
    {
        Url url1 = url("http://www.foo.com/");
        Url url2 = url("http://www.foo.com/");
        boolean result = UrlUtils.isSameDomain(url1, url2, true);
        assertTrue(result);
        
        url1 = url("http://www.foo.com/");
        url2 = url("http://foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, false);
        assertTrue(result);
        
        url1 = url("http://www.foo.com/");
        url2 = url("http://foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertFalse(result);
        
        url1 = url("http://www.foo.com/");
        url2 = url("http://www.bar.com/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertFalse(result);
        
        url1 = url("http://xxx.foo.com/");
        url2 = url("http://xxx.foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, false);
        assertTrue(result);
        
        url1 = url("http://xxx.foo.com/");
        url2 = url("http://xxx.foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertTrue(result);
        
        url1 = url("http://xxx.foo.com/");
        url2 = url("http://yyy.foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, false);
        assertFalse(result);
        
        url1 = url("http://xxx.foo.com/");
        url2 = url("http://yyy.foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertFalse(result);
        
        url1 = url("http://www.foo.com/");
        url2 = url("http://64.233.163.104/");
        result = UrlUtils.isSameDomain(url1, url2, false);
        assertFalse(result);
        
        url1 = url("http://64.233.163.104/");
        url2 = url("http://www.foo.com/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertFalse(result);
        
        url1 = url("http://64.233.163.104/");
        url2 = url("http://64.233.163.104/");
        result = UrlUtils.isSameDomain(url1, url2, true);
        assertTrue(result);
        
        url1 = url("http://64.233.163.104/");
        url2 = url("http://64.233.163.104/");
        result = UrlUtils.isSameDomain(url1, url2, false);
        assertTrue(result);
    }

    Url url(String url)
    {
        return UrlParser.parse(url);
    }
}
