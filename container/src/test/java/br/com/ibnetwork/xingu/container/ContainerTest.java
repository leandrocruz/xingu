package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Container;
import xingu.container.ContainerUtils;
import xingu.container.configuration.ConfigurationManager;
import xingu.container.impl.Pulga;
import xingu.utils.io.FileUtils;
import br.com.ibnetwork.xingu.container.components.Alternative;
import br.com.ibnetwork.xingu.container.components.CircularOne;
import br.com.ibnetwork.xingu.container.components.Component;
import br.com.ibnetwork.xingu.container.components.DefaultExternalConf;
import br.com.ibnetwork.xingu.container.components.My;
import br.com.ibnetwork.xingu.container.components.NoImpl;
import br.com.ibnetwork.xingu.container.components.Simple;
import br.com.ibnetwork.xingu.container.components.SimpleImpl;
import br.com.ibnetwork.xingu.container.components.TestComponent;
import br.com.ibnetwork.xingu.container.components.UsesAnnotations;
import br.com.ibnetwork.xingu.container.components.UsesInject;
import br.com.ibnetwork.xingu.container.components.UsesSimple;
import br.com.ibnetwork.xingu.container.components.impl.ComponentImpl;
import br.com.ibnetwork.xingu.container.components.impl.MyAlternative;
import br.com.ibnetwork.xingu.container.components.impl.MyImpl;
import br.com.ibnetwork.xingu.container.components.impl.NotSoSimple;
import br.com.ibnetwork.xingu.container.components.impl.SimpleMy;

public class ContainerTest
{
    private Container pulga;
    
    @Before
    public void setUp()
    	throws Exception
    {
        pulga = ContainerUtils.getContainer();
    }
    
    @After
    public void tearDown()
    	throws Exception
    {
    	//pulga.stop();
    	ContainerUtils.reset();
        pulga = null;
    }

    @Test
    public void testLookup()
    	throws Exception
    {
        TestComponent simple = (TestComponent) pulga.lookup(Simple.class);
        assertTrue(simple.isConfigured());
        assertTrue(simple.isInitialized());
        assertTrue(simple.isStarted());
        Configuration conf = simple.getConfiguration();
        assertEquals("value",conf.getChild("some").getAttribute("key"));
    }

    @Test
    public void testComposeBefore()
    {
        UsesSimple uses = pulga.lookup(UsesSimple.class);
        Simple simple = pulga.lookup(Simple.class);
        assertSame(simple, uses.getSimple());
    }

    @Test
    public void testComposeAfter()
    {
        Simple simple = pulga.lookup(Simple.class);
        UsesSimple uses = pulga.lookup(UsesSimple.class);
        assertSame(uses.getSimple(),simple);
    }

    @Test
    public void testLookUpWithKey()
    {
        Object alternative = pulga.lookup(Simple.class,"alternative");
        assertTrue(alternative instanceof Simple);
    }
    
    @Test
    public void testExternalConfigurationNoKey()
        throws Exception
    {
        DefaultExternalConf c = pulga.lookup(DefaultExternalConf.class);
        assertTrue(c instanceof ComponentImpl);
        assertEquals("defaultExternalConf",c.getValue());
    }
    
    @Test
    public void testExternalConfigurationUsingKey()
    	throws Exception
    {
        UsesSimple uses = pulga.lookup(UsesSimple.class);
        TestComponent component = (TestComponent) uses;
        assertTrue(component.isConfigured());
        Configuration conf = component.getConfiguration();
        assertEquals("simple",conf.getChild("some").getAttribute("key"));
    }
    
    @Test
    public void testDefaultImplementation()
        throws Exception
    {
        Component c = pulga.lookup(Component.class);
        assertTrue(c instanceof ComponentImpl);
        
        try
        {
            pulga.lookup(NoImpl.class);    
            fail("Should have thrown exception");
        }
        catch(Throwable t)
        {
            //ignored
        }
    }
    
    @Test
    public void testDefaultImplementationInstances()
        throws Exception
    {
        Component c1 = pulga.lookup(Component.class);
        assertTrue(c1 instanceof ComponentImpl);

        Component c2 = pulga.lookup(Component.class);
        assertTrue(c2 instanceof ComponentImpl);
        
        assertSame(c1,c2);
    }
    
    @Test
    public void testDependencyInjectionWithAnnotations()
        throws Exception
    {
        UsesAnnotations component = pulga.lookup(UsesAnnotations.class);
        
        Container container = component.getContainer();
        assertSame(pulga,container);
        
        Simple d1 = component.getD1();
        assertTrue(d1 instanceof SimpleImpl);

        Simple d2 = component.getD2();
        assertTrue(d2 instanceof Alternative);

        Component d3 = component.getD3();
        assertTrue(d3 instanceof ComponentImpl);
    }
    
    @Test
    @Ignore
    public void testLoadNoFile()
    	throws Exception
    {
    	pulga = ContainerUtils.getContainer(null);
    	ConfigurationManager confManager = pulga.lookup(ConfigurationManager.class);
    	Configuration conf = confManager.configurationFor("some-key").getChild("some");
    	assertEquals("value",conf.getAttribute("key"));

    	InputStream is = FileUtils.toInputStream("pulgaEmpty.xml");
    	pulga          = ContainerUtils.getContainer(is);
    	confManager    = pulga.lookup(ConfigurationManager.class);
    	conf = confManager.configurationFor("some-key").getChild("some");
    	assertEquals("value",conf.getAttribute("key"));
    }

    @Test
    public void testInjectAnnotation()
        throws Exception
    {
        UsesInject component = pulga.lookup(UsesInject.class);
        assertNotNull(component.getSimple());
    }

    @Test
    public void testBinder()
        throws Exception
    {
        Binder binder = pulga.binder();
        Simple impl = new NotSoSimple();
        binder.bind(Simple.class).to(impl);
        Simple simple = pulga.lookup(Simple.class);
        assertSame(impl, simple);
    }

    @Test
    public void testBindWithKey()
        throws Exception
    {
        Binder binder = pulga.binder();
        Simple impl = new NotSoSimple();
        binder.bind(Simple.class).to(impl);
        
        Simple impl2 = new Alternative();
        binder.bind(Simple.class, "alternative").to(impl2);
        
        Simple simple = pulga.lookup(Simple.class);
        assertSame(impl, simple);
        
        simple = pulga.lookup(Simple.class, "alternative");
        assertSame(impl2, simple);
    }
    
    @Test
    public void testBindToClass()
        throws Exception
    {
        Binder binder = pulga.binder();
        binder.bind(Simple.class).to(Alternative.class);
        Alternative simple = (Alternative) pulga.lookup(Simple.class);
        assertTrue(simple.isConfigured());
        assertTrue(simple.isInitialized());
        assertTrue(simple.isStarted());

        Alternative other = (Alternative) pulga.lookup(Simple.class);
        assertSame(simple, other);
    }

    @Test
    public void testBindWithConfiguration()
        throws Exception
    {
    	Binder binder = pulga.binder();
        binder.bind(Simple.class).to(Alternative.class).with(new DefaultConfiguration("sample"));
        Alternative simple = (Alternative) pulga.lookup(Simple.class);
        assertTrue(simple.isConfigured());
        assertTrue(simple.isInitialized());
        assertTrue(simple.isStarted());
        assertEquals("sample", simple.getConfiguration().getName());
    }

    @Test
    public void testCircularDependency()
    	throws Exception
    {
    	CircularOne one = pulga.lookup(CircularOne.class);
    	assertEquals("one + two + 3", one.one());
    }

    private Container toContainer(Container parent, String config)
    	throws Exception
    {
    	InputStream is = IOUtils.toInputStream(config);
    	Container result = new Pulga(parent, is);
    	result.configure();
    	result.start();
    	
    	return result;
    }

    @Test
    public void testDefinedNowhere()
    	throws Exception
    {
    	String parentConfig = "<pulga></pulga>";
    	String childConfig  = "<pulga></pulga>";

    	Container parent = toContainer(null, parentConfig);
    	My my = parent.lookup(My.class);
    	assertTrue(my instanceof MyImpl);
    	
    	Container child = toContainer(parent, childConfig);
    	My my2 = child.lookup(My.class);
    	assertTrue(my2 instanceof MyImpl);
    }

    @Test
    public void testDefinedOnlyOnChild()
    	throws Exception
    {
    	String parentConfig = "<pulga></pulga>";
    	String childConfig  = "<pulga><component role=\"br.com.ibnetwork.xingu.container.components.My\" class=\"br.com.ibnetwork.xingu.container.components.impl.SimpleMy\"/></pulga>";

    	Container parent = toContainer(null, parentConfig);
    	My        my     = parent.lookup(My.class);
    	assertTrue(my instanceof MyImpl);
    	
    	Container child = toContainer(parent, childConfig);
    	My        my2   = child.lookup(My.class);
    	assertTrue(my2 instanceof SimpleMy);
    }

    @Test
    public void testDefinedOnlyOnParent()
    	throws Exception
    {
    	String parentConfig = "<pulga><component role=\"br.com.ibnetwork.xingu.container.components.My\" class=\"br.com.ibnetwork.xingu.container.components.impl.SimpleMy\"/></pulga>";
    	String childConfig  = "<pulga></pulga>";

    	Container parent = toContainer(null, parentConfig);
    	My        my     = parent.lookup(My.class);
    	assertTrue(my instanceof SimpleMy);
    	
    	Container child = toContainer(parent, childConfig);
    	My        my2   = child.lookup(My.class);
    	assertTrue(my2 instanceof SimpleMy);
    }

    @Test
    public void testDefinedOnParentAndChild()
    	throws Exception
    {
    	String parentConfig = "<pulga><component role=\"br.com.ibnetwork.xingu.container.components.My\" class=\"br.com.ibnetwork.xingu.container.components.impl.SimpleMy\"/></pulga>";
    	String childConfig  = "<pulga><component role=\"br.com.ibnetwork.xingu.container.components.My\" class=\"br.com.ibnetwork.xingu.container.components.impl.MyAlternative\"/></pulga>";

    	Container parent = toContainer(null, parentConfig);
    	My        my     = parent.lookup(My.class);
    	assertTrue(my instanceof SimpleMy);
    	
    	Container child = toContainer(parent, childConfig);
    	My        my2   = child.lookup(My.class);
    	assertTrue(my2 instanceof MyAlternative);
    }
}
