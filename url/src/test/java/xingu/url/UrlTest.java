package xingu.url;

import static org.junit.Assert.*;

import org.junit.Test;

public class UrlTest
{
    private Url urlFrom(String url)
    {
        return UrlParser.parse(url);
    }

    @Test
    public void testAddParameter()
    {
        String url = "http://www.x.com";
        assertEquals("http://www.x.com?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/";
        assertEquals("http://www.x.com?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/dir1";
        assertEquals("http://www.x.com/dir1?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/dir1/";
        assertEquals("http://www.x.com/dir1/?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/dir1/dir2";
        assertEquals("http://www.x.com/dir1/dir2?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/dir1/dir2?";
        assertEquals("http://www.x.com/dir1/dir2?c=3", urlFrom(url).addParam("c", "3").toString());
        
        url = "http://www.x.com/dir1/dir2?a";
        url = "http://www.x.com/dir1/dir2?a=";
        url = "http://www.x.com/dir1/dir2?a=1";
        url = "http://www.x.com/dir1/dir2?a=1&";
        url = "http://www.x.com/dir1/dir2?a=1&b";
        url = "http://www.x.com/dir1/dir2?a=1&b=";
        url = "http://www.x.com/dir1/dir2?a=1&b=2";

    }
    
    @Test
    public void testMinimumUrl()
    {
        String spec = "http://kidux.net";
        Url url = urlFrom(spec);
        assertNotNull(url);
        
        String host = url.getHost();
        assertEquals("kidux.net", host);
        
        DomainName domainName = url.getDomainName();
        assertNotNull(domainName);
        assertEquals("kidux.net", domainName.fullName());
        assertEquals("kidux.net", domainName.registeredName());
        
        assertEquals(-1, url.getPort());
        assertEquals("http", url.getScheme());
        assertNull(url.getPath());
        assertNull(url.getQueryString());
        assertNull(url.getFragment());
    }
    
    @Test
    public void testMaximumUrl()
    {
        String spec = "http://user:pass@www.kidux.com.br:1234/path/to/file.html?a=AAA&x=XXX#anchor/1";
        Url url = urlFrom(spec);
        assertNotNull(url);
        
        String scheme = url.getScheme();
        assertEquals("http", scheme);
        
        String host = url.getHost();
        assertEquals("www.kidux.com.br", host);
        
        DomainName domainName = url.getDomainName();
        assertNotNull(domainName);
        assertEquals("www.kidux.com.br", domainName.fullName());
        assertEquals("kidux.com.br", domainName.registeredName());
        
        int port = url.getPort();
        assertEquals(1234, port);
        
        String path = url.getPath();
        assertEquals("/path/to/file.html", path);
        
        QueryString queryString = url.getQueryString();
        assertNotNull(queryString);
        assertEquals(2, queryString.size());
        assertEquals("AAA", queryString.get("a"));
        assertEquals("XXX", queryString.get("x"));
        
        String anchor = url.getFragment();
        assertEquals("anchor/1", anchor);
    }
    
    @Test
    public void testLocalhost()
    {
        String spec = "http://localhost";
        Url url = urlFrom(spec);
        assertNotNull(url);
        
        String host = url.getHost();
        assertEquals("localhost", host);
        
        assertEquals("http", url.getScheme());
        assertTrue(url.getDomainName().isLocalHost());
        assertEquals(-1, url.getPort());
        assertNull(url.getPath());
        assertNull(url.getQueryString());
        assertNull(url.getFragment());
    }
    
    @Test
    public void testIPHost()
    {
        String spec = "http://200.100.20.1";
        Url url = urlFrom(spec);
        assertNotNull(url);
        
        String host = url.getHost();
        assertEquals("200.100.20.1", host);
        
        assertEquals("http", url.getScheme());
        assertTrue(url.isIp());
        assertEquals(null, url.getDomainName());
        assertEquals(-1, url.getPort());
        assertEquals(null, url.getPath());
        assertEquals(null, url.getQueryString());
        assertEquals(null, url.getFragment());
    }
    
    @Test
    public void testInvalidUrl()
    {
		try
		{
			String urlStr = "http://kidux net";
			Url url = urlFrom(urlStr);
			fail("Url (" + urlStr + ") not detected.");
		}
		catch (Exception e)
		{
		}
    }
    
    @Test
    public void testToString()
    {
        String spec = "http://www.kidux.com.br";
        Url url = urlFrom(spec);
        assertEquals(spec, url.toString());
    }
    
    @Test
    public void testEquals()
    {
        Url url1 = urlFrom("http://kidux.net");
        Url url2 = urlFrom("http://kidux.net");
        assertTrue(url1.equals(url2));
        assertTrue(url2.equals(url1));
        
        url1 = urlFrom("http://kidux.net");
        url2 = urlFrom("http://foo.org");
        assertFalse(url1.equals(url2));
        assertFalse(url2.equals(url1));
    }
    
    @Test
    public void testSpacesOnUrl()
    {
        String spec = "http://video.globo.com/Videos/Player/Esportes/0,,GIM1364431-7824-PLACAR DA RODADA DESTACA O EMPATE ENTRE FLAMENGO E CORINTHIANS PELO BRASILEIRAO,00.html";
        Url url = urlFrom(spec);
        assertEquals("video.globo.com", url.getHost());
    }

    @Test
    public void testFilenameAndExtension()
    {
        String spec = "http://user:pass@www.kidux.com.br:1234/path/to/file.html?a=AAA&x=XXX#anchor/1";
        Url url = urlFrom(spec);
        assertEquals("file.html", url.getFilename());
        assertEquals("html", url.getExtension());

        Url url2 = urlFrom("http://www.kernel.org/pub/linux/kernel/v3.0/testing/xxxxxxxxxxx");
        assertNull(url2.getExtension());
        
        Url url3 = urlFrom("http://widgets.digg.com/buttons/count?url=http%3A//www.pavablog.com/2012/08/06/marido-se-nega-a-lavar-a-louca-e-e-preso-no-banheiro-pela-mulher-diz-pm/");
        assertNull(url3.getExtension());
    }
}
