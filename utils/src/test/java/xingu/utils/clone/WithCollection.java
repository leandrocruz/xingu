package xingu.utils.clone;

import java.util.List;

public class WithCollection
{
	private List<SimpleObject> list;
	
	public WithCollection()
	{}
	
	public WithCollection(List<SimpleObject> list)
	{
		this.list = list;
	}
	
	public List<SimpleObject> list()
	{
		return list;
	}
}
