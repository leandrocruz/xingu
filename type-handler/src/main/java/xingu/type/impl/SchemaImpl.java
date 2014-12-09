package xingu.type.impl;

import java.util.HashSet;
import java.util.Set;

import xingu.type.Field;
import xingu.type.Schema;

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
