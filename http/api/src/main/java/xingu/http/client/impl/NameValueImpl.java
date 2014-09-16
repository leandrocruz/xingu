package xingu.http.client.impl;

import xingu.http.client.NameValue;

public class NameValueImpl
	implements NameValue
{

	private String	name;

	private String	value;

	private String	type;

	public NameValueImpl(String name, String value)
	{
		this.name  = name;
		this.value = value;
	}

	public NameValueImpl(String name, String value, String type)
	{
		this.name  = name;
		this.value = value;
		this.type  = type;
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
	public String getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return name + ": " + value + (type == null ? "" : "("+type+")");
	}
}
