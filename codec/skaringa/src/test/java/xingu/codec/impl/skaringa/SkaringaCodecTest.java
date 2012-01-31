package xingu.codec.impl.skaringa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xingu.codec.Codec;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class SkaringaCodecTest
    extends XinguTestCase
{
    @Inject
    private Codec codec;
    
//    @Override
//	protected String getContainerFile()
//	{
//    	return "pulga-empty.xml";
//	}

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
}
