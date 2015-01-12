package xingu.utils.clone;


public class WithArray
{
	private SimpleObject[] array;
	
	public WithArray()
	{}
	
	public WithArray(SimpleObject[] array)
	{
		this.array = array;
	}
	
	public SimpleObject[] array()
	{
		return array;
	}
}
