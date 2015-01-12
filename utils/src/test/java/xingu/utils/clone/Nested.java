package xingu.utils.clone;

public class Nested
{
	private int i;
	
	private SimpleObject simple;

	public Nested()
	{}
	
	public Nested(int i, SimpleObject simple)
	{
		this.i = i;
		this.simple = simple;
	}

	public SimpleObject simple()
	{
		return simple;
	}

	@Override
	public boolean equals(Object obj)
	{
		Nested other = (Nested) obj;
		return i == other.i && simple.equals(other.simple);
	}
}
