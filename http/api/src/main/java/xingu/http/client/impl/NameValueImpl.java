package xingu.http.client.impl;

import xingu.http.client.NameValue;

public class NameValueImpl
	implements NameValue
{

	private String	name;

	private String	value;

	public NameValueImpl(String name, String value)
	{
		this.name  = name;
		this.value = value;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return name + ": " + value;
	}
	
	
}
