package xingu.pdf.impl;

import java.util.ArrayList;
import java.util.List;

import xingu.pdf.Line;
import xingu.pdf.Region;

import com.adobe.pdfjt.services.textextraction.Word;

public class PdfParseContext
{
	private List<Line> lines = new ArrayList<Line>();

	public void processWord(Word word)
		throws Exception
	{
		Line line = lineFor(word);
		if(line == null)
		{
			double y = word.bottomRight().y();
			line = new Line(new Region(y, 2.0), word.getPageNumber() - 1 /* ZERO BASED */);
			lines.add(line);
		}
		line.add(word);
	}

	private Line lineFor(Word word)
		throws Exception
	{
		double y    = word.bottomRight().y();
		int    page = word.getPageNumber() - 1;
		for(Line line : lines)
		{
			int    linePage = line.getPage();
			Region region   = line.getRegion();
			if(linePage == page && region.inRadius(y))
			{
				return line;
			}
		}
		return null;
	}
	
	public List<Line> getLines()
	{
		return lines;
	}
}
