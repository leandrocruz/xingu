package xingu.codec.simple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xingu.codec.Codec;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class SimpleCodecTest
	extends XinguTestCase
{
	@Inject
	private Codec codec;

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(Codec.class).to(SimpleCodec.class);
	}
	
	@Test
	public void testSerialize()
		throws Exception
	{
		String xml = codec.encode("sample");
		assertEquals("<string>sample</string>", xml);
		
		Parent parent = new Parent(10, "sample", new Child(1));
		parent.children.add(new Child(2));
		parent.children.add(new Child(3));
		xml = codec.encode(parent);
		
		System.out.println(xml);
		Parent decoded = codec.decode(xml, Parent.class);
		assertEquals(10, decoded.i);
		assertEquals("sample", decoded.s);
		assertEquals(1, decoded.c.i);
		assertEquals(2, decoded.children.get(0).i);
		assertEquals(3, decoded.children.get(1).i);
	}
}
