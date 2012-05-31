package br.com.ibnetwork.xingu.utils.clone;

import java.util.Map;

public class WithMap
{
	private Map<String, SimpleObject> map;
	
	public WithMap()
	{}
	
	public WithMap(Map<String, SimpleObject> map)
	{
		this.map = map;
	}
	
	public Map<String, SimpleObject> map()
	{
		return map;
	}
}
