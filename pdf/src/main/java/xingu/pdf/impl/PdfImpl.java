package xingu.pdf.impl;

import java.util.ArrayList;
import java.util.List;

import com.adobe.pdfjt.services.textextraction.Word;

import xingu.pdf.Line;
import xingu.pdf.Pdf;

public class PdfImpl
	implements Pdf
{
	private List<Line> lines = new ArrayList<Line>();
	
	private int lineNumber = 0;
	
	public void addLine(Line line)
	{
		line.setNumber(lineNumber++);
		lines.add(line);
	}

	@Override
	public Line getLine(int lineNumber)
	{
		int size = lines.size();
		if(lineNumber >= size)
		{
			return null;
		}
		return lines.get(lineNumber);
	}

	@Override
	public Line find(String text)
	{
		TextFinder finder = new TextFinder(text);
		for(Line line : lines)
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
		return null;
	}
}

