package br.com.ibnetwork.xingu.template.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.com.ibnetwork.xingu.template.Context;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class ContextImpl
	implements Context
{
    private Map<String, Object> map;
    
    public ContextImpl(int size)
    {
        map = new HashMap<String, Object>(size);
    }
    
    public Map getMap()
    {
        return map;
    }

    public void put(String key, Object value)
    {
        map.put(key,value);
    }

    public void delete(String key)
    {
        map.remove(key);
    }

    public boolean contains(String key)
    {
        return map.containsKey(key);
    }
    
    public Object get(String key)
    {
        return map.get(key);
    }
    
    public void clear()
    {
        map.clear();
    }

    public Set getKeys()
    {
        return map.keySet();
    }

    public int getSize()
    {
        return map.size();
    }
}
