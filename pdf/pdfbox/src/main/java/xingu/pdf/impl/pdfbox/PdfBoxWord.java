package xingu.pdf.impl.pdfbox;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Coordinate;
import xingu.pdf.PdfException;
import xingu.pdf.Word;

public class PdfBoxWord
	implements Word
{
	private int	page;

	private String	text;

	public PdfBoxWord(int page, String text)
	{
		this.page = page;
		this.text = text;
	}

	@Override
	public Coordinate bottomRight()
		throws PdfException
	{
		throw new NotImplementedYet();
	}

	@Override
	public Coordinate topLeft()
		throws PdfException
	{
		throw new NotImplementedYet();
	}

	@Override
	public int pageNumber()
	{
		return page;
	}

	@Override
	public String text()
	{
		return text;
	}
}
