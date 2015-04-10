package xingu.utils.io.chunk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.util.List;

import org.junit.Test;

import xingu.utils.ByteUtils;
import xingu.utils.io.chunk.impl.FrameImpl;
import xingu.utils.io.chunk.impl.FramedChunkImpl;
import xingu.utils.io.chunk.impl.FramedChunkReaderImpl;
import xingu.utils.io.chunk.impl.FramedChunkWriterImpl;

public class FramedChunkParserTest
{

	@Test
	public void testWriteAndParseFile()
		throws Exception
	{
		Frame[] frames;

		File file  = File.createTempFile("chunk-", ".data");
		FramedChunkWriter writer = new FramedChunkWriterImpl(file);

		FramedChunk c1 = new FramedChunkImpl(3);
		c1.add(new FrameImpl("This is a string"));
		c1.add(new FrameImpl("This is another string"));
		c1.add(new FrameImpl(1));
		writer.write(c1);

		FramedChunk c2 = new FramedChunkImpl(3);
		c2.add(new FrameImpl("a"));
		c2.add(new FrameImpl(Frame.LAZY, "b".getBytes()));
		c2.add(new FrameImpl("á"));
		writer.write(c2);

		writer.close();
		
		FramedChunkReader reader = new FramedChunkReaderImpl(file);
		List<FramedChunk> chunks = reader.read();
		assertEquals(2, chunks.size());

		/*
		 * Test Chunk 1
		 */
		FramedChunk c1New = chunks.get(0);
		assertNotSame(c1, c1New);
		frames = c1New.getFrames();
		assertEquals(3, frames.length);
		assertEquals("This is a string", new String(frames[0].getPayload()));
		assertEquals("This is another string", new String(frames[1].getPayload()));
		assertEquals(1, ByteUtils.toInt(frames[2].getPayload()));
		
		/*
		 * Test Chunk 2
		 */
		FramedChunk c2New = chunks.get(1);
		assertNotSame(c2, c2New);
		
		frames = c2New.getFrames();
		assertEquals(3, frames.length);
		assertEquals("a", new String(frames[0].getPayload()));
		assertEquals("b", new String(frames[1].getPayload()));
		assertEquals("á", new String(frames[2].getPayload()));
		
		reader.close();
	}
}