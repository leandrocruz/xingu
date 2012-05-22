package xingu.url;

import static org.junit.Assert.*;

import xingu.url.impl.DomainNameImpl;

import org.junit.Test;

public class DomainNameTest
{
    @Test
    public void testFullName()
    {
        assertEquals("registro.br", fullName("registro.br"));
        assertEquals("kidux.net", fullName("kidux.net"));
        assertEquals("kidux.com.br", fullName("kidux.com.br"));
        assertEquals("www.kidux.net", fullName("www.kidux.net"));
        assertEquals("www.kidux.com.br", fullName("www.kidux.com.br"));
    }

    @Test
    public void testRegisteredName()
    {
        assertEquals("registro.br", registeredName("registro.br"));
        assertEquals("registro.br", registeredName("www.registro.br"));
        assertEquals("kidux.net", registeredName("kidux.net"));
        assertEquals("kidux.net", registeredName("www.kidux.net"));
        assertEquals("kidux.com.br", registeredName("kidux.com.br"));
        assertEquals("kidux.com.br", registeredName("www.kidux.com.br"));
    }
    
    @Test
    public void testIsTopLevelDomain()
    {
        assertTrue(isTopLevelDomain("net"));
        assertFalse(isTopLevelDomain("kidux.net"));
        assertFalse(isTopLevelDomain("www.kidux.net"));
        
        assertTrue(isTopLevelDomain("br"));
        assertFalse(isTopLevelDomain("registro.br"));
        assertFalse(isTopLevelDomain("www.registro.br"));
        
        assertTrue(isTopLevelDomain("com.br"));
        assertFalse(isTopLevelDomain("kidux.com.br"));
        assertFalse(isTopLevelDomain("www.kidux.com.br"));
    }
    
    private boolean isTopLevelDomain(String domainName)
    {
        DomainName domain = new DomainNameImpl(domainName);
        return domain.isTopLevelDomain();
    }

    private String fullName(String domainName)
    {
        DomainName domain = new DomainNameImpl(domainName);
        return domain.fullName();
    }
    
    private String registeredName(String domainName)
    {
        DomainName domain = new DomainNameImpl(domainName);
        return domain.registeredName();
    }
}