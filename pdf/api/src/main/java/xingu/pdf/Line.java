package xingu.pdf;

import java.util.ArrayList;
import java.util.List;

import xingu.utils.StringUtils;


public class Line
	implements Comparable<Line>
{
	private int        number;

	private int        page;

	private Region     region;

	private List<Word> words   = new ArrayList<Word>();

	public Line(Region region, int page)
	{
		this.region = region;
		this.page   = page;
	}

	public void add(Word word)
		throws Exception
	{
		boolean vertical = isVertical(word);
		if(vertical)
		{
			//System.err.println(word + " (" + angle + ")");
			return;
		}

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

	private boolean isVertical(Word word)
		throws Exception
	{
		Coordinate br = word.bottomRight();
		Coordinate tl = word.topLeft();
		double    angle = tl.angleTo(br);
		return angle > 0;
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

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public Word getWord(int i)
	{
		return words.get(i);
	}

	public int getPage()
	{
		return page;
	}

	@Override
	public int compareTo(Line other)
	{
		if(page == other.page)
		{
			return (int) (other.region.center - region.center );
		}
		else
		{
			return page - other.page;
		}
	}

	@Override
	public String toString()
	{
		return "[p#"+page+" w#"+words.size()+" "+number+"] " + asText();
	}
}