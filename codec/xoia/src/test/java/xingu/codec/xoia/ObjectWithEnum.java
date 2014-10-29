package xingu.codec.xoia;

public class ObjectWithEnum
{
	private String	value	= "some string";

	private Type	type;

	public enum Type
	{
		TYPE_A,
		TYPE_B
	}

	public ObjectWithEnum()
	{}

	public ObjectWithEnum(Type type)
	{
		this.type = type;
	}
}