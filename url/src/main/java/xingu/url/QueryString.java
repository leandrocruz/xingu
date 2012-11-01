package xingu.url;

import java.io.Serializable;
import java.util.Set;

public interface QueryString
    extends Serializable
{
    String get(String name);
    
    Set<String> names();

    int size();

    QueryString add(String name, String value);

    String getDecoded(String name);

    String getDecoded(String name, String encoding);
}