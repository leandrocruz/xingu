package xingu.codec.xoia;

public class ObjectWithObjectArray
{
	private Object[]	args;

	public ObjectWithObjectArray()
	{}

	public ObjectWithObjectArray(Object... args)
	{
		this.args = args;
	}
	
	public Object get(int pos)
	{
		return args[pos];
	}
}