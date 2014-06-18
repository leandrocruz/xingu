package xingu.pdf.impl;

import java.util.ArrayList;
import java.util.List;

import xingu.pdf.Line;
import xingu.pdf.Region;

import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.services.textextraction.Word;

public class PdfParseContext
{
	private List<Line> lines = new ArrayList<Line>();
	
	public PdfParseContext(PDFDocument pdf)
	{}

	public void processWord(Word word)
		throws Exception
	{
		double y = word.bottomRight().y();
		Line line = lineFor(y);
		if(line == null)
		{
			line = new Line(new Region(y, 2.0));
			lines.add(line);
		}
		line.add(word);
	}

	private Line lineFor(double y)
	{
		for(Line line : lines)
		{
			Region region = line.getRegion();
			if(region.inRadius(y))
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
