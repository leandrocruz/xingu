package br.com.ibnetwork.xingu.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.test.ArrayConstructor;
import br.com.ibnetwork.xingu.factory.test.Implementation;
import br.com.ibnetwork.xingu.factory.test.Interface;
import br.com.ibnetwork.xingu.factory.test.MultipleConstructors;
import br.com.ibnetwork.xingu.factory.test.My;
import br.com.ibnetwork.xingu.factory.test.InvisibleConstructors;
import br.com.ibnetwork.xingu.factory.test.UsesFactory;
import br.com.ibnetwork.xingu.factory.test.impl.MyImpl;

public class FactoryTest
    extends XinguTestCase
{
	@Inject
    private Factory factory;
    
	@Test
    public void testCreateObject()
    	throws Exception
    {
        Object obj = factory.create(Object.class);
        assertNotNull(obj);
    }
    
	@Test
    public void testCreateList()
    	throws Exception
    {
    	List<?> list = factory.create(List.class);
    	assertTrue(list instanceof ArrayList);
    }

	@Test
    public void testCreateSet()
		throws Exception
	{
    	Set<?> set = factory.create(Set.class);
    	assertTrue(set instanceof TreeSet);
	}
    
	@Test
    public void testCreateMap()
		throws Exception
	{
    	Map<?,?> map = factory.create(Map.class);
    	assertTrue(map instanceof HashMap);
	}

	@Test
    public void testDefaultImpl()
        throws Exception
    {
        My my = factory.create(My.class);
        assertTrue(my instanceof MyImpl);
        assertEquals("default", my.getValue());
        
        my = factory.create(My.class, "some");
        assertTrue(my instanceof MyImpl);
        assertEquals("some", my.getValue());
    }
    
	@Test
    public void testLifecycle()
    	throws Exception
    {
        UsesFactory obj = factory.create(UsesFactory.class);
        assertTrue(obj.isConfigured());
        assertTrue(obj.initialized());
        assertTrue(obj.started());
    }


	@Test
    public void testMapping()
    	throws Exception
    {
        Interface obj = factory.create(Interface.class);
        assertTrue(obj instanceof Implementation);
        assertNull(obj.getString());
        assertEquals(0, obj.getInteger());
    }

	@Test
    public void testConstructorWithParameters()
    {
        Interface obj = factory.create(Interface.class, "test", 100);
        assertTrue(obj instanceof Implementation);
        assertEquals("test", obj.getString());
        assertEquals(100, obj.getInteger());
    }

	@Test
	@Ignore
    public void testArrayConstructor()
        throws Exception
    {
        Object[] params = new Object[]{"String",new Integer(10)};
        ArrayConstructor obj = factory.create(ArrayConstructor.class, params);
        assertEquals(String.class,obj.params[1].getClass());
        assertEquals(Integer.class,obj.params[2].getClass());
    }
    
	@Test
    public void testMultipleConstructors()
        throws Exception
    {
        Object[] params = null;
        MultipleConstructors obj = null;
        Class<MultipleConstructors> clazz = MultipleConstructors.class;
        
        obj = factory.create(clazz);
        testObject(obj, null, null);

        obj = factory.create(clazz, params);
        testObject(obj, null, null);

        obj = factory.create(clazz, "string");
        testObject(obj,"string",null);

        obj = factory.create(clazz, "string2", 1);
        testObject(obj, "string2", 1);

        try
        {
            obj = factory.create(clazz, "string", 1l);
            fail("Should have thrown exception");
        }
        catch(Exception e)
        {
            //ignored
        }

    }
    
    @Test
    public void testInvisibleConstructors()
    {
        Class<InvisibleConstructors> clazz = InvisibleConstructors.class;
        InvisibleConstructors obj = null;
        
        obj = factory.create(clazz);
        assertNull(obj.s);
        assertNull(obj.i);

        obj = factory.create(clazz, "hello");
        assertEquals("hello", obj.s);
        assertNull(obj.i);

        obj = factory.create(clazz, new Integer(321));
        assertEquals(new Integer(321), obj.i);
        assertNull(obj.s);
    }

    private void testObject(MultipleConstructors obj, String s, Integer i)
    {
        assertEquals(s,obj.s);
        assertEquals(i,obj.i);
    }
}
