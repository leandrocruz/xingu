package xingu.codec.xoia;

import org.junit.Ignore;
import org.junit.Test;

import xingu.codec.Codec;
import xingu.codec.impl.xoia.XoiaCodec;
import xingu.codec.xoia.ObjectWithEnum.Type;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class XoiaCodecTest
	extends XinguTestCase
{
	@Inject
	private Codec codec;
	
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(Codec.class).to(XoiaCodec.class);
	}


	@Test
	@Ignore
	public void testEnum()
		throws Exception
	{
		String xml = codec.encode(new ObjectWithEnum(Type.TYPE_A));
		System.out.println(xml);
		Object decoded = codec.decode(xml);
		
	}
}
