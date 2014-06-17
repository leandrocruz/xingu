package xingu.pdf;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

import com.adobe.pdfjt.core.fontset.PDFFontSet;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.services.textextraction.TextExtractor;
import com.adobe.pdfjt.services.textextraction.Word;
import com.adobe.pdfjt.services.textextraction.WordsIterator;

public class Teste
{
	List<Line> lines = new ArrayList<Line>();
	
	
	public static void main(String[] args)
		throws Exception
	{
		ClassLoader cl  = Thread.currentThread().getContextClassLoader();
		URL         url = cl.getResource("5007323.pdf");
		new Teste().process(url.getFile());
	}

	private void process(String file)
		throws Exception
	{
		PDFDocument pdf = ApplicationUtils.loadPDFDocument(file);

		PDFFontSet    fontset   = SampleFontLoaderUtil.buildSampleFontSet();
		TextExtractor extractor = TextExtractor.newInstance(pdf,fontset);
		WordsIterator it        = extractor.getWordsIterator();
		
		while (it.hasNext())
		{
			Word word = it.next();
			processWord(word);
		}
		
		Collections.sort(lines);
		int i = 0;
		for(Line line : lines)
		{
			Region region = line.getRegion();
			System.out.println((++i) + ". on Region " + region);
			List<Word> words = line.getWords();
			for(Word word : words)
			{
				System.out.println("\t'" + word.toString() + "' at " + word.bottomRight());
			}
			System.out.println("");
		}
	}

	private void processWord(Word word)
		throws Exception
	{
		double y    = word.bottomRight().y();
		Line   line = lineFor(y);
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
}