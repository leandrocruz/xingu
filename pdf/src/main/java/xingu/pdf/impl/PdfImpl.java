package xingu.pdf.impl;

import java.util.ArrayList;
import java.util.List;

import xingu.pdf.Line;
import xingu.pdf.Pdf;

public class PdfImpl
	implements Pdf
{
	private List<Line> lines = new ArrayList<Line>();
	
	public void addLine(Line line)
	{
		lines.add(line);
	}

	@Override
	public Line getLine(int lineNumber)
	{
		return lines.get(lineNumber);
	}
}
