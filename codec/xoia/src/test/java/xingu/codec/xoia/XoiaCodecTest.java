package xingu.codec.xoia;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import xingu.codec.Codec;
import xingu.codec.impl.xoia.XoiaCodec;
import xingu.codec.xoia.ObjectWithEnum.Type;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;

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

	@Test
	public void testObjectArray()
		throws Exception
	{
		Object[] array = new Object[]{"um", 2, new Long(3)};
		String xml = codec.encode(new ObjectWithObjectArray(array));
		//System.out.println(xml);
		ObjectWithObjectArray decoded = (ObjectWithObjectArray) codec.decode(xml);
		assertEquals(array[0], decoded.get(0));
		assertEquals(array[1], decoded.get(1));
		assertEquals(array[2], decoded.get(2));
	}
}
