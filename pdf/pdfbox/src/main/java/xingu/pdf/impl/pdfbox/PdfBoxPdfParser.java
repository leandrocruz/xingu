package xingu.pdf.impl.pdfbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Pdf;
import xingu.pdf.impl.PdfImpl;
import xingu.pdf.impl.PdfParserSupport;

public class PdfBoxPdfParser
	extends PdfParserSupport
{
	private static final boolean DEBUG = true;

	@Override
	public Pdf parse(InputStream is)
		throws Exception
	{
        PDFParser parser = new PDFParser(is);
        parser.parse();
        PDDocument doc = parser.getPDDocument();
        if(doc.isEncrypted())
        {
            throw new NotImplementedYet("Document is encrypted");
        }
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        stripper.setLineSeparator("\n");
        stripper.setWordSeparator(" ");

        PdfImpl result = new PdfImpl();

        int lineNumber = 0;
        int pages = doc.getNumberOfPages();
        for(int i = 1 ; i <= pages ; i++)
        {
        	String text = textForPage(doc, stripper, i);
        	String[] lines = text.split("\n");
	        for(String line : lines)
			{
	        	PdfBoxLine l = new PdfBoxLine(i - 1, line, lineNumber++);
				result.addLine(l);
				if(DEBUG) System.out.println(l);
			}
        }
        doc.close();

        return result;
	}

	private String textForPage(PDDocument doc, PDFTextStripper stripper, int page)
		throws IOException
	{
		stripper.resetEngine();
		stripper.setStartPage(page);
		stripper.setEndPage(page);
		StringWriter writer = new StringWriter();
		stripper.writeText(doc, writer);
		return writer.toString();
	}
}