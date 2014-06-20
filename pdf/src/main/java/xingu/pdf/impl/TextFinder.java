package xingu.pdf.impl;

import br.com.ibnetwork.xingu.utils.StringUtils;

public class TextFinder
{
	private String[]	words;

	private int index = 0;
	
	public TextFinder(String words)
	{
		this.words = words.split(StringUtils.SPACE);
	}

	public TextFinder(String[] words)
	{
		this.words = words;
	}

	public void consume(String input)
	{
		String next = words[index];
		if(next.equals(input))
		{
			index++;
		}
		else
		{
			index = 0;
		}
	}

	public boolean done()
	{
		return index == words.length;
	}
}
