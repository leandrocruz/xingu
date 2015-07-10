package xingu.pdf.impl.pdfbox;

import java.util.ArrayList;
import java.util.List;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Line;
import xingu.pdf.Word;

public class PdfBoxLine
	implements Line
{
	private int	page;
	
	private int	number;

	private String	text;
	
	private List<Word> words;

	public PdfBoxLine(int page, String text, int number)
	{
		this.page   = page;
		this.number = number;
		this.text   = text;
		this.words  = parse();
	}

	private List<Word> parse()
	{
		String[] parts = text.split(" ");
		List<Word> words = new ArrayList<>(parts.length);
		for(String w : parts)
		{
			words.add(new PdfBoxWord(page, w));
		}
		return words;
	}

	@Override
	public String text()
	{
		return text;
	}

	@Override
	public int number()
	{
		return number;
	}

	@Override
	public void number(int i)
	{}

	@Override
	public int page()
	{
		return page;
	}

	@Override
	public Word word(int i)
	{
		return words.get(i);
	}

	@Override
	public List<Word> words()
	{
		return words;
	}

	@Override
	public Word lastWord()
	{
		int idx = words().size() - 1;
		return word(idx);
	}

	@Override
	public String toString()
	{
		return "[p#"+page+" "+number+" w#"+words.size()+"] " + text();
	}
}
