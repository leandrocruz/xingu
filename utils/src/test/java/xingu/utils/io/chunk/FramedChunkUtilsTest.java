package xingu.utils.io.chunk;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Ignore;
import org.junit.Test;

public class FramedChunkUtilsTest
{
	@Test
	//@Ignore
	public void testPrint()
		throws Exception
	{
		FramedChunkUtils.print(new File("/home/leandro/oystr/20150714.131732-6fdw5y"), System.out);
	}
}
