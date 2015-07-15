package xingu.utils.io.chunk;

import java.io.File;

import org.junit.Test;

import xingu.utils.io.chunk.impl.PrintChunkVisitor;
import xingu.utils.io.chunk.impl.SeekingFramedChunkReader;

public class FramedChunkUtilsTest
{
	@Test
	//@Ignore
	public void testPrint()
		throws Exception
	{
		File              file    = new File("/home/leandro/oystr/journal/20150714.131732-6fdw5y");
		FramedChunkReader reader  = new SeekingFramedChunkReader(file);
		ChunkVisitor      visitor = new PrintChunkVisitor(System.out);
		FramedChunkUtils.print(reader, visitor, System.out);
	}
}
