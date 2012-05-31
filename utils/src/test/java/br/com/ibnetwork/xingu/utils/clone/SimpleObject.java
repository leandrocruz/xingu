package br.com.ibnetwork.xingu.utils.clone;

public class SimpleObject
{
	private int i;
	
	private String s;

	public SimpleObject()
	{}
	
	public SimpleObject(int i, String s)
	{
		this.i = i;
		this.s = s;
	}

	@Override
	public boolean equals(Object obj)
	{
		SimpleObject other = (SimpleObject) obj;
		return i == other.i && s.equals(other.s);
	}

	@Override
	public String toString()
	{
		return "'" + i + " - " + s + "'";
	}
}
