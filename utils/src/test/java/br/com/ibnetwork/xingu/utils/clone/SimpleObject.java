package br.com.ibnetwork.xingu.utils.clone;

public class SimpleObject
{
	private int i;
	
	private String s;
	
	private String w;

	public SimpleObject()
	{}
	
	public SimpleObject(int i, String s, String w)
	{
		this.i = i;
		this.s = s;
		this.w = w;
	}

	@Override
	public boolean equals(Object obj)
	{
		SimpleObject other = (SimpleObject) obj;
		return i == other.i && s.equals(other.s) && w.equals(other.w);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": '" + i + " - " + s + "/" + w + "'";
	}

	public String s()
	{
		return s;
	}

	public String w()
	{
		return w;
	}

	public int i()
	{
		return i;
	}

	
}
