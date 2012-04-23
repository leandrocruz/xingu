package xingu.template;

import java.util.Map;
import java.util.Set;

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
