package xingu.type.impl;

import java.util.ArrayList;
import java.util.List;

import xingu.type.Field;
import xingu.type.Transformer;

public class FieldImpl
	implements Field
{
	private String	name;

	private String	column;
	
	private List<Transformer> transformers = new ArrayList<>();

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

	@Override
	public void add(Transformer t)
	{
		if(t != null)
		{
			transformers.add(t);
		}
	}

	@Override
	public List<Transformer> getFilters()
	{
		return transformers;
	}
}
