package br.com.ibnetwork.xingu.utils.type;

import java.util.Set;

public interface Schema
{
	Set<Field> getFields();

	void add(Field field);
}
