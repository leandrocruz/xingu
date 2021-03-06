package xingu.xls;

import java.util.HashMap;
import java.util.Map;

import xingu.utils.ValueProvider;

public class Record
	implements ValueProvider<String>
{
	private Map<String, String> map = new HashMap<String, String>();
	
	public String get(String name)
	{
		return map.get(name);
	}

	public void add(String name, String value)
	{
		map.put(name, value);
	}

	public Map<String, String> toMap()
	{
		return map;
	}
}