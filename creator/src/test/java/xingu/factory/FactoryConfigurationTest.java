package xingu.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.factory.Factory;
import xingu.factory.test.IsConfigurable;
import xingu.factory.test.IsConfigurable2;

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
