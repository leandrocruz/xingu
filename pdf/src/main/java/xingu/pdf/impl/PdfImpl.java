package xingu.pdf.impl;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adobe.pdfjt.services.textextraction.Word;

import xingu.pdf.Line;
import xingu.pdf.Pdf;

public class PdfImpl
	implements Pdf
{
	private List<Line> lines = new ArrayList<Line>();
	
	private int lineNumber = 0;

	private int pageCount = 0;
	
	private String	file;
	
	public PdfImpl(String file)
	{
		this.file = file;
	}

	public void addLine(Line line)
	{
		pageCount = line.getPage() + 1;
		line.setNumber(lineNumber++);
		lines.add(line);
	}

	@Override
	public Line getLine(int lineNumber)
	{
		return getLineFrom(lineNumber, lines);
	}

	@Override
	public Line getLine(int lineNumber, int page)
	{
		List<Line> lines = byPage(page);
		return getLineFrom(lineNumber, lines);
	}

	private Line getLineFrom(int lineNumber, List<Line> lines)
	{
		int size = lines.size();
		if(lineNumber >= size)
		{
			return null;
		}
		return lines.get(lineNumber);
	}

	private List<Line> byPage(int page)
	{
		List<Line> result = new ArrayList<Line>();
		for(Line line : lines)
		{
			if(line.getPage() == page)
			{
				result.add(line);
			}
		}
		return result;
	}

	@Override
	public Line find(String text, int page)
	{
		TextFinder finder = new TextFinder(text);
		for(Line line : lines)
		{
			if(line.getPage() == page)
			{
				List<Word> words = line.getWords();
				for(Word word : words)
				{
					String s = word.toString();
					finder.consume(s);
					if(finder.done())
					{
						return line;
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getFile()
	{
		return file;
	}

	@Override
	public List<Line> getLines()
	{
		return lines;
	}

	@Override
	public void printTo(Writer writer)
		throws Exception
	{
		for(Line line : lines)
		{
			writer.write(line.toString() + "\n");
		}
	}

	@Override
	public int getPageCount()
	{
		return pageCount;
	}

	@Override
	public String getText()
	{
		StringBuffer buffer = new StringBuffer();
		List<Line>   lines  = getLines();
		for(Line line : lines)
		{
			String asText = line.asText();
			if(StringUtils.isEmpty(asText))
			{
				continue;
			}
			buffer.append(asText).append("\n");
		}
		return buffer.toString();
	}
}