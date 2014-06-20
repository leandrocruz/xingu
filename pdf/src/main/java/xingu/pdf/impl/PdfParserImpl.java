package xingu.pdf.impl;

import java.util.List;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

import xingu.pdf.ApplicationUtils;
import xingu.pdf.Line;
import xingu.pdf.Pdf;
import xingu.pdf.PdfParser;
import xingu.pdf.SampleFontLoaderUtil;

import com.adobe.pdfjt.core.fontset.PDFFontSet;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.services.textextraction.TextExtractor;
import com.adobe.pdfjt.services.textextraction.Word;
import com.adobe.pdfjt.services.textextraction.WordsIterator;

public class PdfParserImpl
	implements PdfParser
{

	@Override
	public Pdf parse(String file)
		throws Exception
	{
		PDFDocument     pdf       = ApplicationUtils.loadPDFDocument(file);
		PdfParseContext ctx       = new PdfParseContext();

		PDFFontSet      fontset   = SampleFontLoaderUtil.buildSampleFontSet();
		TextExtractor   extractor = TextExtractor.newInstance(pdf, fontset);
		WordsIterator   it        = extractor.getWordsIterator();

		while(it.hasNext())
		{
			Word word = it.next();
			ctx.processWord(word);
		}
		
		List<Line> lines = ctx.getLines();
		Collections.sort(lines);

		PdfImpl result = new PdfImpl(file);
		for(Line line : lines)
		{
			result.addLine(line);
		}
		
		return result;
	}
}
