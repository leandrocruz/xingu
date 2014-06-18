package xingu.pdf;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class PdfParserTest
	extends XinguTestCase
{
	@Inject
	private PdfParser parser;

	@Test
	public void test()
		throws Exception
	{
		ClassLoader cl  = Thread.currentThread().getContextClassLoader();
		URL         url = cl.getResource("5007323.pdf");
		Pdf pdf = parser.parse(url.getFile());
		Line line0 = pdf.getLine(0);
		assertEquals("BANCO GMAC S / A", line0.asText());
	}
}