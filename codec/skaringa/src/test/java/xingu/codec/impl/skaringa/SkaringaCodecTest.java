package xingu.codec.impl.skaringa;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.skaringa.javaxml.ObjectTransformer;
import com.skaringa.javaxml.ObjectTransformerFactory;

import xingu.codec.Codec;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class SkaringaCodecTest
    extends XinguTestCase
{
    @Inject
    private Codec codec;

	@Override
    protected void rebind(Binder binder)
    {
        binder.bind(Codec.class).to(SkaringaCodec.class);
    }

    @Test
    public void test()
        throws Exception
    {
        String input = "{\"beanId\":0,\"id\":\"id\",\"os\":\"os\",\"name\":\"name\",\"kiduxHome\":\"home\",\"accounts\":[\"acc1\",\"acc2\"],\"localAccounts\":null}";
        SimpleMachine machine = codec.decode(input, SimpleMachine.class);
        assertEquals("id", machine.id());
        assertEquals("os", machine.os());
        assertEquals("name", machine.name());
        assertEquals("home", machine.kiduxHome());
        
        String output = codec.encode(machine);
        assertEquals(input, output);
    }
    
    @Test
    @Ignore
    public void testSample()
    	throws Exception
    {
    	ObjectTransformer transformer = ObjectTransformerFactory.getInstance().getImplementation();
    	transformer.setProperty(javax.xml.transform.OutputKeys.INDENT, "true");
    	transformer.setProperty(com.skaringa.javaxml.PropertyKeys.OMIT_XSI_TYPE, "true");
    	transformer.setProperty(com.skaringa.javaxml.PropertyKeys.SKIP_UNKNOWN_FIELDS, "true");
    	
    	SimpleMachine m      = new SimpleMachine();
    	m.beanId             = 1;
    	m.id                 = "machine";
    	m.kiduxHome          = "Home";
    	String        string = transformer.serializeToString(m);
    	System.out.println(string);
    }
}
