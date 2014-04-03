package br.com.ibnetwork.xingu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils
{
    public static <K,V> List<MapElement<K, V>> toJSON(Map<K,V> map) {
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
    	return false;
    }
}
