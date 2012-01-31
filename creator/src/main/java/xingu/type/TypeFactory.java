package xingu.type;

import java.util.Set;

public interface TypeFactory
{
    Class<?> classByType(String type);
    
    Object objectByType(String type);

    String typeOf(Object object);

    Set<String> types();
}
