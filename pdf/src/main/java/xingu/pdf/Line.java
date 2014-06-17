package xingu.pdf;

import java.util.ArrayList;
import java.util.List;

import com.adobe.pdfjt.services.textextraction.Word;

public class Line
	implements Comparable<Line>
{
	private Region		region;

	private List<Word>	words	= new ArrayList<Word>();

	public Line(Region region)
	{
		this.region = region;
	}

	public void add(Word word)
	{
		words.add(word);
	}

	public Region getRegion()
	{
		return region;
	}

	public List<Word> getWords()
	{
		return words;
	}

	@Override
	public int compareTo(Line other)
	{
		return (int) (other.region.center - region.center );
	}
}