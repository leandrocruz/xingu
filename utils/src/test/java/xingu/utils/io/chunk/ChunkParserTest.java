package xingu.utils.io.chunk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.junit.Test;

import xingu.utils.ByteUtils;

public class ChunkParserTest
{
	@Test
	public void testWriteAndParseFile()
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