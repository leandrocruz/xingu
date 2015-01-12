package xingu.pdf;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class PdfParserTest
	extends XinguTestCase
{
	@Inject
	private PdfParser parser;

	@Test
	public void test()
		throws Exception
	{
		ClassLoader cl    = Thread.currentThread().getContextClassLoader();
		URL         url   = cl.getResource("5007323.pdf");
		Pdf         pdf   = parser.parse(url.getFile());
		Line        line = pdf.getLine(0);
		assertEquals("BANCO GMAC S / A", line.asText());

		url   = cl.getResource("ContractPaymentHistory-3640624759922585494.pdf");
		pdf   = parser.parse(url.getFile());

		line = pdf.getLine(0);
		assertEquals("BANCO GMAC S / A", line.asText());

		line = pdf.getLine(0, 0);
		assertEquals("BANCO GMAC S / A", line.asText());

		line = pdf.getLine(41);
		assertEquals("BANCO GMAC S / A", line.asText());

		line = pdf.getLine(0, 1);
		assertEquals("BANCO GMAC S / A", line.asText());
	}

	@Test
	public void testFindTest()
		throws Exception
	{
		ClassLoader cl   = Thread.currentThread().getContextClassLoader();
		URL         url  = cl.getResource("ContractPaymentHistory-3640624759922585494.pdf");
		Pdf         pdf  = parser.parse(url.getFile());

//		Writer writer = new StringWriter();
//		pdf.printTo(writer);
//		System.out.println(writer.toString());
		
		Line line = pdf.find("Payment History :", 0);
		assertEquals(0, line.getPage());
		assertEquals(19, line.getNumber());

		line = pdf.find("Payment History :", 1);
		assertEquals(1, line.getPage());
		assertEquals(60, line.getNumber());

	}
}