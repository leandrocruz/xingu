package br.com.ibnetwork.xingu.template;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface Context
{
    Map getMap();
    
    void put(String key, Object value);

    Object get(String key);
    
    void delete(String key);
    
    boolean contains(String key);
    
    Set getKeys();
    
    void clear();
    
    int getSize();
    
}
