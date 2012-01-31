package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.configuration.ConfigurationManager;

@Ignore
public class ConfigurationManagerTest
    extends XinguTestCase
{
    @Inject
	private ConfigurationManager confManager;
    
    @Test
    public void getConfiguration()
    	throws Exception
    {
        Configuration conf = confManager.configurationFor("some-key");
        assertEquals("some-key",conf.getAttribute("key"));
        Configuration child = conf.getChild("some");
        assertEquals("value",child.getAttribute("key"));
    }
}
