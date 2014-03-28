package xingu.netty.protocol.frame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;

public class FrameTest
{
	@Test
	public void testEmptyFramePacking()
		throws Exception
	{
		byte[]        unpacked = null;
		Frame         frame    = null;
		ChannelBuffer buffer   = null;
		int           size     = 0;
		
		// Null Payload
		frame  = new ByteArrayFrame(null);
		buffer = frame.pack();
		size   = buffer.readInt();
		assertEquals("Null payloads should have NEGATIVE size", -1, size);

		buffer.resetReaderIndex();
		unpacked = Frame.unpack(buffer);
		assertNull("Null payloads should unpack as null", unpacked);
		
		// Empty Payload
		frame  = new ByteArrayFrame(new byte[]{});
		buffer = frame.pack();
		size   = buffer.readInt();
		assertEquals("Empty payloads should have ZERO size", 0, size);

		buffer.resetReaderIndex();
		unpacked = Frame.unpack(buffer);
		assertEquals("Empty payloads should unpack as empty byte arrays", 0, unpacked.length);
	}

	@Test
	public void testFramePacking()
		throws Exception
	{
		String        input  = "This is just a String: áéíóú!!ç";
		byte[]        data   = input.getBytes();
		Frame         frame  = new ByteArrayFrame(data);
		ChannelBuffer buffer = frame.pack();
		int           size   = buffer.readInt();
		assertEquals(data.length, size);
	}

	@Test
	public void testCompositeFramePacking()
		throws Exception
	{
		Frame[] frames = new Frame[]{
				new StringFrame("a"),
				new StringFrame("b"),
				new StringFrame("c"),
		};

		ChannelBuffer buffer = Frame.packArray(frames);
		assertEquals("should be INT_LEN + 2*INT_LEN + 3*(INT_LEN+1)", 3*(1+Frame.INT_LEN) + 3*Frame.INT_LEN, buffer.capacity());

		byte[][] unpacked = Frame.unpackArray(buffer);
		assertEquals(3, unpacked.length);
		assertEquals("a", new String(unpacked[0]));
		assertEquals("b", new String(unpacked[1]));
		assertEquals("c", new String(unpacked[2]));
	}

	@Test
	public void testCompositeFramePackingWithNulls()
		throws Exception
	{
		Frame[] frames = new Frame[]{
				new ByteArrayFrame(null),	/* Frame.INT_LEN + Frame.INT_LEN */
				new StringFrame("123"), 	/* 3 + Frame.INT_LEN */
				new ByteArrayFrame(null),
				new StringFrame("123"),
		};

		ChannelBuffer buffer = Frame.packArray(frames);

		byte[][] unpacked = Frame.unpackArray(buffer);
		assertEquals(4, unpacked.length);
		assertEquals(null, unpacked[0]);
		assertEquals("123", new String(unpacked[1]));
		assertEquals(null, unpacked[2]);
		assertEquals("123", new String(unpacked[3]));
	}

}