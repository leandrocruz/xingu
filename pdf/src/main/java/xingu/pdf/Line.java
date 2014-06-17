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
		throws Exception
	{
		int idx = indexFor(word);
		words.add(idx, word);
	}

	private int indexFor(Word word)
		throws Exception
	{
		double my   = word.bottomRight().x();
		int    size = words.size() - 1;
		for(int i = 0; i < size; i++)
		{
			Word w = words.get(i);
			double x = w.bottomRight().x();
			if(my < x)
			{
				return i == 0 ? i : i - 1;
			}
		}

		return words.size();
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