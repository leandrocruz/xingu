package xingu.maze;

import java.util.List;

public interface Domain
{
    String name();
    
    ClassLoader classLoader();
    
    List<String> entries();
}