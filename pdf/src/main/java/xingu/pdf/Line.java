package xingu.pdf;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.StringUtils;

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
		if(idx < 0)
		{
			words.add(word);
		}
		else
		{
			words.add(idx, word);
		}
	}

	private int indexFor(Word word)
		throws Exception
	{
		double my   = word.bottomRight().x();
		int    size = words.size();
		if(size == 0)
		{
			return 0;
		}

		for(int i = 0; i < size; i++)
		{
			Word w = words.get(i);
			double x = w.bottomRight().x();
			if(my < x)
			{
				return i;
			}
		}
		return -1;
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

	public String asText()
	{
		StringBuffer sb = new StringBuffer();
		int i = 0;
		int size = words.size();
		for(Word word : words)
		{
			i++;
			sb.append(word.toString());
			if(i < size)
			{
				sb.append(StringUtils.SPACE);
			}
		}
		return sb.toString();
	}
}