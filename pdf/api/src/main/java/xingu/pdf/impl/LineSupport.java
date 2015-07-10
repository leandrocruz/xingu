package xingu.pdf.impl;

import xingu.pdf.Line;
import xingu.pdf.Word;

public abstract class LineSupport
	implements Line
{
	protected int	number;

	protected int	page;

	@Override
	public int number()
	{
		return number;
	}

	@Override
	public void number(int number)
	{
		this.number = number;
	}

	@Override
	public int page()
	{
		return page;
	}

	@Override
	public Word lastWord()
	{
		int idx = words().size() - 1;
		return word(idx);
	}

	@Override
	public String toString()
	{
		return "[p#"+page+" "+number+" w#"+words().size()+"] " + text();
	}
}
