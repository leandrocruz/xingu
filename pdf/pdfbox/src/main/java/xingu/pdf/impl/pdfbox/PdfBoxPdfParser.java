package xingu.pdf.impl.pdfbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Pdf;
import xingu.pdf.PdfParser;

public class PdfBoxPdfParser
	implements PdfParser
{

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
        return new PdfBoxAdapter(doc);
	}

	@Override
	public Pdf parse(String file)
		throws Exception
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(new File(file));
			return parse(is);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
}