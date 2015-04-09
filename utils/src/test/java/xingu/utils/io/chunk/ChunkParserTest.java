package xingu.utils.io.chunk;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.junit.Test;

import xingu.utils.ByteUtils;
import xingu.utils.io.chunk.impl.FrameImpl;
import xingu.utils.io.chunk.impl.IChunkImpl;
import xingu.utils.io.chunk.impl.IChunkReaderImpl;
import xingu.utils.io.chunk.impl.IChunkWriterImpl;

public class ChunkParserTest
{

	@Test
	public void testWriteAndParseFile()
		throws Exception
	{
		Frame[] frames;

		File file  = File.createTempFile("chunk-", ".data");
		IChunkWriter writer = new IChunkWriterImpl(file);

		IChunk c1 = new IChunkImpl(3);
		c1.add(new FrameImpl("This is a string"));
		c1.add(new FrameImpl("This is another string"));
		c1.add(new FrameImpl(1));
		writer.write(c1);

		IChunk c2 = new IChunkImpl(3);
		c2.add(new FrameImpl("a"));
		c2.add(new FrameImpl(Frame.LAZY, "b".getBytes()));
		c2.add(new FrameImpl("á"));
		writer.write(c2);

		writer.close();
		
		IChunkReader reader = new IChunkReaderImpl(file);
		List<IChunk> chunks = reader.read();
		assertEquals(2, chunks.size());

		/*
		 * Test Chunk 1
		 */
		IChunk c1New = chunks.get(0);
		assertNotSame(c1, c1New);
		frames = c1New.getFrames();
		assertEquals(3, frames.length);
		assertEquals("This is a string", new String(frames[0].getPayload()));
		assertEquals("This is another string", new String(frames[1].getPayload()));
		assertEquals(1, ByteUtils.toInt(frames[2].getPayload()));
		
		/*
		 * Test Chunk 2
		 */
		IChunk c2New = chunks.get(1);
		assertNotSame(c2, c2New);
		
		frames = c2New.getFrames();
		assertEquals(3, frames.length);
		assertEquals("a", new String(frames[0].getPayload()));
		assertEquals("b", new String(frames[1].getPayload()));
		assertEquals("á", new String(frames[2].getPayload()));
		
		reader.close();
	}
	
	@Test
	public void testWriteAndParseFileOld()
		throws Exception
	{
		File         file  = File.createTempFile("chunk-", ".data");
		OutputStream os    = new FileOutputStream(file);

		ChunkWriter writer = new ChunkWriter(os);
		writer.write("This is a string");
		writer.write("This another string");
		writer.write(1);
		writer.close();

		InputStream is = new FileInputStream(file);
		List<Chunk> chunks = new ChunkReader(is).read();
		assertEquals(3, chunks.size());
		assertEquals("This is a string" , ByteUtils.toString(chunks.get(0).getBytes()));
		assertEquals("This another string", ByteUtils.toString(chunks.get(1).getBytes()));
		assertEquals(1, ByteUtils.toInt(chunks.get(2).getBytes()));
	}

	@Test
	public void testTruncatedFile()
		throws Exception
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("io/truncated.data");
		List<Chunk> chunks = new ChunkReader(is).read();
		assertEquals(3, chunks.size());
		
		Chunk chunk = chunks.get(0);
		assertEquals(false, chunk.isTruncated());
		assertEquals("This is a string" , ByteUtils.toString(chunk.getBytes()));
		
		chunk = chunks.get(1);
		assertEquals(false, chunk.isTruncated());
		assertEquals("This another string", ByteUtils.toString(chunk.getBytes()));
		
		chunk = chunks.get(2);
		assertEquals(true, chunk.isTruncated());
		assertEquals(2, chunk.getRead());
	}

	@Test
	public void testNoPayloadFile()
		throws Exception
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("io/nopayload.data");
		List<Chunk> chunks = new ChunkReader(is).read();
		assertEquals(1, chunks.size());
		Chunk chunk = chunks.get(0);
		assertEquals(true, chunk.isTruncated());
		assertEquals(-1, chunk.getRead());
	}
}