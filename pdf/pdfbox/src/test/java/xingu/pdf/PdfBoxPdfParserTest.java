package xingu.pdf;

import java.io.InputStream;

import org.junit.Test;

import xingu.container.Binder;
import xingu.container.Inject;
import xingu.container.XinguTestCase;
import xingu.pdf.impl.pdfbox.PdfBoxPdfParser;

public class PdfBoxPdfParserTest
	extends XinguTestCase
{
	@Inject
	private PdfParser parser;
	
	@Override
	protected void rebind(Binder binder)
		throws Exception
	{
		binder.bind(PdfParser.class).to(PdfBoxPdfParser.class);
	}
	
	@Test
	public void testParse()
		throws Exception
	{
		InputStream is = getClass().getClassLoader().getResourceAsStream("boleto.pdf");
		Pdf pdf = parser.parse(is);
	}
}
