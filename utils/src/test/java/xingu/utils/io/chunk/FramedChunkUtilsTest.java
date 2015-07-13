package xingu.utils.io.chunk;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Ignore;
import org.junit.Test;

public class FramedChunkUtilsTest
{
	@Test
	@Ignore
	public void testPrint()
		throws Exception
	{
		Writer writer = new StringWriter();
		FramedChunkUtils.print(new File("/home/leandro/oystr/20150713.073713-h8agco"), writer);
		System.out.println(writer);
	}
}
