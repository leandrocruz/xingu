package xingu.url;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpParserTest
{
    @Test
    public void testFullUrl()
        throws Exception
    {
        Url url = UrlParser.parse("http://leandro:pwd@kidux.com.br:8080/dir?a=1&b=2#home");
        assertEquals("http", url.getScheme());
        //assertEquals("leandro:pwd", url.userInfo());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(8080, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testHttps()
        throws Exception
    {
        Url url = UrlParser.parse("https://leandro:pwd@kidux.com.br:8080/dir?a=1&b=2#home");
        assertEquals("https", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(8080, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testNoAuth()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br:65000/dir?a=1&b=2#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(65000, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testNoAuthNoPort()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/dir?a=1&b=2#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testNoAuthNoPortNoPath()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/?a=1&b=2#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());

        url = UrlParser.parse("http://kidux.com.br?a=1&b=2#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testNoAuthNoPortNoPathNoQuery()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());

        url = UrlParser.parse("http://kidux.com.br#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testNoAuthNoPortNoPathNoQueryNoFragment()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());

        url = UrlParser.parse("http://kidux.com.br");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testNoAuthNoPortNoQuery()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/dir#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testEmptyFragment()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());

        url = UrlParser.parse("http://kidux.com.br#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testAllButFragment()
        throws Exception
    {
        Url url = UrlParser.parse("http://leandro:pwd@kidux.com.br:8080/dir?a=1&b=2#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(8080, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://leandro:pwd@kidux.com.br:8080/dir?a=1&b=2");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(8080, url.getPort());
        assertEquals("/dir", url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testEmptyQuery()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/?#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());

        url = UrlParser.parse("http://kidux.com.br?#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());
    }

    @Test
    public void testQuery()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/?a=1&b=2");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals(null, url.getFragment());
    
        url = UrlParser.parse("http://kidux.com.br?a=1&b=2");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals("a=1&b=2", url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testNoPassword()
        throws Exception
    {
        Url url = UrlParser.parse("http://leandro@kidux.com.br/");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://leandro@kidux.com.br");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testHostAndPort()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br:80/");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals("/", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br:80");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testPortAndQuery()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br:80?a=1");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals("a=1", url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br:80?");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testPortAndFragment()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br:80#home");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("home", url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br:80#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(80, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testControlChars()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/?#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br/?");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());

        url = UrlParser.parse("http://kidux.com.br?#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());

        url = UrlParser.parse("http://kidux.com.br/#");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br#/?");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("/?", url.getFragment());

        url = UrlParser.parse("http://kidux.com.br#?/");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("?/", url.getFragment());

    }
    
    @Test
    public void testFragment()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/?#?a=1");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("?a=1", url.getFragment());

        url = UrlParser.parse("http://kidux.com.br/#?a=1");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("?a=1", url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br#?a=1");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals("?a=1", url.getFragment());
    }    

    @Test
    public void testPath()
        throws Exception
    {
        Url url = UrlParser.parse("http://kidux.com.br/dir1/dir2/");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/dir1/dir2/", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    
        url = UrlParser.parse("http://kidux.com.br/dir1//");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/dir1//", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br/dir1");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("/dir1", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
        
        url = UrlParser.parse("http://kidux.com.br//");
        assertEquals("http", url.getScheme());
        assertEquals("kidux.com.br", url.getHost());
        assertEquals(-1, url.getPort());
        assertEquals("//", url.getPath());
        assertEquals(null, url.getQuery());
        assertEquals(null, url.getFragment());
    }

    @Test
    public void testAboutBlank()
    	throws Exception
    {
    	Url url = UrlParser.parse("about:blank");
    	url = UrlParser.parse("localhost");
    	url = UrlParser.parse("about:config");
    }
}
