package xingu.taster;

import java.io.InputStream;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class ByteTasterTest
	extends XinguTestCase
{
	@Inject
	private ByteTaster taster;

	
	@Override
	protected String getContainerFile()
	{
		return "pulga-empty.xml";
	}

	@Test
	public void testTaste()
		throws Exception
	{
		String[] files = new String[] {
				"files/sample1.png",
				"files/sample2.png",
				"files/sample1.jpg",
		};

		for (String file : files)
		{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			byte[] bytes = new byte[4];
			is.read(bytes);
			System.out.println(file + ": " + taster.mimeFrom(bytes));
		}

	}
}
