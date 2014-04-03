package br.com.ibnetwork.xingu.utils.inspector;

import java.util.List;
import java.util.Map;

import br.com.ibnetwork.xingu.utils.ArrayUtils;

public class NestedObject
{
	int						i;

	NestedObject			me;

	String[]				array = new String[]{"a", "b", "c"};

	List<String>			list;

	Map<Integer, String>	map;

	public NestedObject()
	{}
	
	public NestedObject(int i)
	{
		this.i = i;
	}
	
	public NestedObject(int i, NestedObject nested)
	{
		this.i = i;
		this.me = nested;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof NestedObject))
		{
			return false;
		}
		
		NestedObject other = (NestedObject) obj;
		return other.i == i
				&& ArrayUtils.equals(other.array, array);
	}
}
