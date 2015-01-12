package xingu.utils.clone;

import java.util.Map;

import xingu.utils.MapUtils;

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

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof WithMap))
		{
			return false;
		}
		
		WithMap other = (WithMap) obj;
		return MapUtils.equals(map, other.map);
	}
}
