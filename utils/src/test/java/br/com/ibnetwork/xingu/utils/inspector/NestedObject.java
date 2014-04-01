package br.com.ibnetwork.xingu.utils.inspector;

import java.util.List;
import java.util.Map;

public class NestedObject
{
	int							i;

	NestedObject				nested;

	NestedObject[]				array;

	List<NestedObject>			list;

	Map<Integer, NestedObject>	map;

	public NestedObject(int i)
	{
		this.i = i;
	}
	
	public NestedObject(int i, NestedObject nested)
	{
		this.i = i;
		this.nested = nested;
	}

}
