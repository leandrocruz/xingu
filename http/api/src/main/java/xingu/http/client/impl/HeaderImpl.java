package xingu.http.client.impl;

import xingu.http.client.Header;

public class HeaderImpl
	implements Header
{

	private String	name;

	private String	value;

	public HeaderImpl(String name, String value)
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
