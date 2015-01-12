package xingu.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.type.TypeFactory;
import xingu.type.test.Ack;
import xingu.type.test.Ping;

public class TypeFactoryTest
    extends XinguTestCase
{
    @Inject
    private TypeFactory typeFactory;

    @Override
	protected String getContainerFile()
	{
		return "pulga.xml";
	}

    @Test
    public void testTypeMapping()
        throws Exception
    {
        String type;

        //mapped
        type = typeFactory.typeOf(new Ack());
        assertEquals("ack", type);

        //using package prefix
        type = typeFactory.typeOf(new Ping());
        assertEquals("test.Ping", type);
        
        //no mapping
        type = typeFactory.typeOf(new Object());
        assertEquals("java.lang.Object", type);
    }

    @Test
    public void testClassByType()
        throws Exception
    {
        Class<?> clazz;
        
        //mapped
        clazz = typeFactory.classByType("ack");
        assertEquals(Ack.class, clazz);

        //using package prefix
        clazz = typeFactory.classByType("test.Ping");
        assertEquals(Ping.class, clazz);

        //no mapping
        clazz = typeFactory.classByType("java.lang.Object");
        assertEquals(Object.class, clazz);
    }
}
