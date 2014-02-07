package xingu.maze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import xingu.maze.Config;
import xingu.maze.Domain;
import xingu.maze.Utils;

public class ClassLoaderTest
{
    private Config config()
        throws Exception
    {
        String file = Utils.configurationFileName();
        return Utils.config(file);
    }
    
    @Test
    public void testDomain()
        throws Exception
    {
        Config config = config();
        assertEquals("net.kidux.maze.FakeMain", config.mainClass());
        
        Domain defaultDomain = config.defaultDomain();
        assertEquals("default", defaultDomain.name());

        Domain domain = config.domain("foo");
        assertEquals("foo", domain.name());
    }
    
    @Test
    public void testLoadJavaLang()
        throws Exception
    {
        Config config = config();
        Class<?> clazz;
        
        clazz = config.defaultDomain().classLoader().loadClass("java.lang.String");
        assertSame(String.class, clazz);
        
        clazz = config.domain("foo").classLoader().loadClass("java.lang.String");
        assertSame(String.class, clazz);
    }
    
    @Test(expected=ClassNotFoundException.class)
    public void testLoadClassNotInDomain()
        throws Exception
    {
        config().defaultDomain().classLoader().loadClass("org.apache.commons.lang.StringUtils");
    }

    @Test
    public void testLoadClass()
        throws Exception
    {
        Domain domain = config().domain("foo");
        Class<?> clazz = domain.classLoader().loadClass("org.apache.commons.lang.StringUtils");
        assertSame(domain, clazz.getClassLoader());
    }

    @Test
    public void testWildCardExpansion()
        throws Exception
    {
        Domain domain = config().domain("wildCard");
        List<String> entries = domain.entries();
        assertEquals(2, entries.size());
    }

    @Test
    public void testRelativePaths()
        throws Exception
    {
        Config config = config();
        Class<?> clazz = config.domain("relative").classLoader().loadClass("br.com.ibnetwork.xingu.l10n.LocalizationManager");
        assertEquals(clazz.getName(), "br.com.ibnetwork.xingu.l10n.LocalizationManager");
    }
}
