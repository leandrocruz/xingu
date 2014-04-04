package br.com.ibnetwork.xingu.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtils
{
    public static <K,V> List<MapElement<K, V>> toJSON(Map<K,V> map)
    {
        List<MapElement<K, V>> result = new ArrayList<MapElement<K, V>>(map.size());
        for (K key : map.keySet())
        {
            result.add(new MapElement<K, V>(key, map.get(key)));
        }
        
        return result;
    }
    
    public static final boolean equals(Map m1, Map m2)
    {
    	if(m1.isEmpty())
    	{
    		return m2.isEmpty();
    	}
    	
    	Set keys1 = m1.keySet();
    	Set keys2 = m2.keySet();
    	
    	if(!keys1.equals(keys2))
    	{
    		return false;
    	}

    	Iterator it = keys1.iterator();
    	while(it.hasNext())
    	{
    		Object key = it.next();
    		Object value1 = m1.get(key);
    		Object value2 = m1.get(key);
    		if(!value1.equals(value2))
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }
}
