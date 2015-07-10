package xingu.pdf.impl.pdfbox;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Line;
import xingu.pdf.Pdf;

public class PdfBoxAdapter
	implements Pdf
{

	private PDDocument	doc;

	public PdfBoxAdapter(PDDocument doc)
	{
		this.doc = doc;
	}

	@Override
	public String getFile()
	{
		return null;
	}

	@Override
	public Line getLine(int lineNumber)
	{
		return null;
	}

	@Override
	public Line getLine(int lineNumber, int page)
	{
		return null;
	}

	@Override
	public List<Line> getLines()
	{
		return null;
	}

	@Override
	public Line find(String text, int page)
	{
		return null;
	}

	@Override
	public void printTo(Writer writer)
		throws Exception
	{}

	@Override
	public int getPageCount()
	{
		return 0;
	}

	@Override
	public String getText()
	{
		try
		{
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stripper.setWordSeparator(" ");
			StringWriter writer = new StringWriter();
			stripper.writeText(doc, writer);
			return writer.toString();
		}
		catch(IOException e)
		{
			throw new NotImplementedYet(e);
		}
	}

	@Override
	public String get(Pattern pattern)
	{
		return null;
	}

}
