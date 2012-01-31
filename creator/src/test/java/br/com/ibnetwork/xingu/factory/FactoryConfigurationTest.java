package br.com.ibnetwork.xingu.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.test.IsConfigurable;
import br.com.ibnetwork.xingu.factory.test.IsConfigurable2;

public class FactoryConfigurationTest
    extends XinguTestCase
{
    @Inject
    private Factory factory;
    
    @Test
    @Ignore
    public void testExternalConfiguration()
        throws Exception
    {
        IsConfigurable obj = factory.create(IsConfigurable.class);
        assertTrue(obj.isConfigured());
        Configuration conf = obj.getConfiguration();
        assertEquals("value2",conf.getChild("some").getAttribute("key"));
    }

    @Test
    @Ignore
    public void testInlineConfiguration()
        throws Exception
    {
        IsConfigurable2 obj = factory.create(IsConfigurable2.class);
        assertTrue(obj.isConfigured());
        Configuration conf = obj.getConfiguration();
        assertEquals("inline",conf.getChild("some").getAttribute("key"));
    }

}
