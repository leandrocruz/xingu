package xingu.utils;

public class NameValue<V>
{
	public String	name;

	public V		value;

	public NameValue(String name, V value)
	{
		this.name  = name;
		this.value = value;
	}
}
