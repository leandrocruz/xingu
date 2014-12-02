package br.com.ibnetwork.xingu.utils.type.impl;

import br.com.ibnetwork.xingu.utils.type.Field;

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
