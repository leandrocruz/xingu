package br.com.ibnetwork.xingu.utils.clone;

public class Transient
{
	private int i1;
	
	private transient int i2;
	
	public Transient()
	{}

	public Transient(int i1, int i2)
	{
		this.i1 = i1;
		this.i2 = i2;
	}

	@Override
	public boolean equals(Object obj)
	{
		Transient other = (Transient) obj;
		return i1 == other.i1 && i2 == other.i2;
	}

	public int i1()
	{
		return i1;
	}

	public int i2()
	{
		return i2;
	}
}
