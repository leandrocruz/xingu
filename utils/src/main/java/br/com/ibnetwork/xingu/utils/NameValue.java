package br.com.ibnetwork.xingu.utils;

public class NameValue<V>
{
	String	name;

	V		value;

	public NameValue(String name, V value)
	{
		this.name  = name;
		this.value = value;
	}
}
