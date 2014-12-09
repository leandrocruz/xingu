package xingu.type.impl;

import xingu.type.Field;

public class FieldImpl
	implements Field
{
	private String	name;

	private String	column;

	public FieldImpl(String name, String column)
	{
		this.name   = name;
		this.column = column;
	}

	@Override
	public String getColumn()
	{
		return column;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
