package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;


public class EnvironmentTest
    extends XinguTestCase
{
    @Inject
    private Environment env;

    @Test
    public void testReplaceVars() 
        throws Exception
    {
        String dir = FilenameUtils.separatorsToUnix(System.getProperty("user.dir"));
        String home = FilenameUtils.separatorsToUnix(System.getProperty("user.home"));

        String replacement = env.replaceVars("${app.root}/dir/subdir");
        assertEquals(dir + "/dir/subdir", replacement);

        replacement = env.replaceVars("${app.root}:${user.home}:${app.root}");
        assertEquals(dir + ":" + home + ":" + dir, replacement);

        replacement = env.replaceVars("${app.root}/${foo}");
        assertEquals(dir + "/${foo}", replacement);
        
        replacement = env.replaceVars("${who} junk");
        assertEquals("${who} junk", replacement);

    }
    
    @Test
    public void testDefaultValues()
        throws Exception
    {
        String replacement;
        
        replacement = env.replaceVars("${who?me}");
        assertEquals("me", replacement);

        replacement = env.replaceVars("${who?/tmp/}");
        assertEquals("/tmp/", replacement);

        replacement = env.replaceVars("${who?c:\\Program Files\\MyApp}");
        assertEquals("c:/Program Files/MyApp", replacement);
    }
}
