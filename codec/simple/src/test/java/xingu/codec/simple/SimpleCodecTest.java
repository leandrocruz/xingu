package xingu.codec.simple;

import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.configuration.Configuration;
import org.junit.Test;

import xingu.codec.Codec;
import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class SimpleCodecTest
	extends XinguTestCase
{
	@Inject
	private Codec codec;

	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		Configuration conf = this.buildFrom("<x><classAttribute suppress=\"false\"/></x>");
		binder.bind(Codec.class).to(SimpleCodec.class).with(conf);
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

		Parent decoded = codec.decode(xml, Parent.class);
		assertEquals(10, decoded.i);
		assertEquals("sample", decoded.s);
		assertEquals(1, decoded.c.i);
		assertEquals(2, decoded.children.get(0).i);
		assertEquals(3, decoded.children.get(1).i);
	}
}
