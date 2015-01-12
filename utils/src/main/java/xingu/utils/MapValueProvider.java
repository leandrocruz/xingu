package xingu.utils;

import java.util.Map;

public class MapValueProvider<T>
	implements ValueProvider<T>
{
	private Map<String, T>	map;

	public MapValueProvider(Map<String, T> map)
	{
		this.map = map;
	}
	
	@Override
	public T get(String name)
	{
		return map.get(name);
	}
}
