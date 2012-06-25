package xingu.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HttpUrlTest
{
    private static final String SCHEME = "http";
    
    private static final String USER_AND_PASS = "john_doe-1:my_secret";
    
    private static final String HOST_OR_DOMAIN = "www.kix-kidux.net";
    
    private static final int PORT = 8080;
    
    private static final String PATH = "/path-to/some/thing_cool-123.html";
    
    private static final String QUERY = "q=Kids+Education&lang=en-US&meta=";
    
    private static final String ANCHOR = "some_anchor-1/abc/123";

    private static final String COMPLETE_URL = SCHEME+"://"+USER_AND_PASS+"@"+HOST_OR_DOMAIN+":"+PORT+PATH+"?"+QUERY+"#"+ANCHOR;

    private Url completeUrl;
    
    private Url hostOnly;
    
    private Url hostAndPort;

    private Url hostAndPath;

    private Url hostAndQuery;
    
    private Url hostPathAndQuery;
    
    private Url hostPortAndQuery;
    
    @Before
    public void setUp()
    {
        completeUrl = UrlParser.parse(COMPLETE_URL);
        hostOnly = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN);
        hostAndPort = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN+":"+PORT);
        hostAndPath = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN+PATH);
        hostAndQuery = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN+"?"+QUERY);
        hostPathAndQuery = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN+PATH+"?"+QUERY);
        hostPortAndQuery = UrlParser.parse(SCHEME+"://"+HOST_OR_DOMAIN+":"+PORT+"?"+QUERY);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNullUrl()
    {
        UrlParser.parse(null);
    }

    @Test
    public void testScheme()
    {
        assertEquals(SCHEME, completeUrl.getScheme());
        assertEquals(SCHEME, hostOnly.getScheme());
        assertEquals(SCHEME, hostAndPort.getScheme());
        assertEquals(SCHEME, hostAndPath.getScheme());
        assertEquals(SCHEME, hostAndQuery.getScheme());
        assertEquals(SCHEME, hostPathAndQuery.getScheme());
        assertEquals(SCHEME, hostPortAndQuery.getScheme());
    }
    
    @Test
    public void testHost()
    {
        assertEquals(HOST_OR_DOMAIN, completeUrl.getHost());
        assertEquals(HOST_OR_DOMAIN, hostOnly.getHost());
        assertEquals(HOST_OR_DOMAIN, hostAndPort.getHost());
        assertEquals(HOST_OR_DOMAIN, hostAndPath.getHost());
        assertEquals(HOST_OR_DOMAIN, hostAndQuery.getHost());
        assertEquals(HOST_OR_DOMAIN, hostPathAndQuery.getHost());
        assertEquals(HOST_OR_DOMAIN, hostPortAndQuery.getHost());
    }
    
    @Test
    public void testDomainName()
    {
        assertEquals(HOST_OR_DOMAIN, completeUrl.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostOnly.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostAndPort.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostAndPath.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostAndQuery.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostPathAndQuery.getDomainName().fullName());
        assertEquals(HOST_OR_DOMAIN, hostPortAndQuery.getDomainName().fullName());
    }

    @Test
    public void testPort()
    {
        assertEquals(PORT, completeUrl.getPort());
        assertEquals(PORT, hostAndPort.getPort());
        assertEquals(PORT, hostPortAndQuery.getPort());
        assertEquals(-1, hostOnly.getPort());
        assertEquals(-1, hostAndPath.getPort());
        assertEquals(-1, hostAndQuery.getPort());
        assertEquals(-1, hostPathAndQuery.getPort());
    }

    @Test
    public void testPath()
    {
        assertEquals(PATH, completeUrl.getPath());
        assertEquals(PATH, hostAndPath.getPath());
        assertEquals(PATH, hostPathAndQuery.getPath());
        assertEquals(null, hostOnly.getPath());
        assertEquals(null, hostAndPort.getPath());
        assertEquals(null, hostAndQuery.getPath());
        assertEquals(null, hostPortAndQuery.getPath());
    }

    @Test
    public void testQueryString()
    {
        assertEquals(QUERY, completeUrl.getQueryString().toString());
        assertEquals(QUERY, hostAndQuery.getQueryString().toString());
        assertEquals(QUERY, hostPathAndQuery.getQueryString().toString());
        assertEquals(QUERY, hostPortAndQuery.getQueryString().toString());
        assertNull(hostOnly.getQueryString());
        assertNull(hostAndPort.getQueryString());
        assertNull(hostAndPath.getQueryString());
    }

    @Test
    public void testAnchor()
    {
        assertEquals(ANCHOR, completeUrl.getFragment());
        assertNull(hostAndPath.getFragment());
        assertNull(hostPathAndQuery.getFragment());
        assertNull(hostOnly.getFragment());
        assertNull(hostAndPort.getFragment());
        assertNull(hostAndQuery.getFragment());
        assertNull(hostPortAndQuery.getFragment());
    }

    @Test
    public void testIsFavIcon()
    {
        assertTrue(UrlParser.parse("http://x.com/favicon.ico").isFavIcon());
        assertTrue(UrlParser.parse("http://x.com/favicon.ico?").isFavIcon());
        assertTrue(UrlParser.parse("http://x.com/favicon.ico?query").isFavIcon());
        assertTrue(UrlParser.parse("http://x.com/favicon.ico#anchor").isFavIcon());
        assertFalse(UrlParser.parse("http://x.com/y/favicon.ico").isFavIcon());
        assertFalse(UrlParser.parse("http://x.com/#anchor/favicon.ico").isFavIcon());
        assertFalse(UrlParser.parse("http://x.com/secretPath?favicon.ico").isFavIcon());
    }
}
