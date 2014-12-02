package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.HashSet;
import java.util.Set;

import br.com.ibnetwork.xingu.utils.type.Field;
import br.com.ibnetwork.xingu.utils.type.Schema;

public class SchemaImpl
	implements Schema
{
	private Set<Field> fields = new HashSet<>();
	
	@Override
	public Set<Field> getFields()
	{
		return fields;
	}

	@Override
	public void add(Field field)
	{
		fields.add(field);
	}
}
