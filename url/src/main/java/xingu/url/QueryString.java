package xingu.url;

import java.io.Serializable;
import java.util.Set;

import br.com.ibnetwork.xingu.utils.collection.FluidMap;

public interface QueryString
    extends Serializable
{
    String get(String name);
    
    Set<String> names();

    int size();

    QueryString add(String name, String value);

    String getDecoded(String name);

    String getDecoded(String name, String charset);
    
    FluidMap<String> toMap();

	FluidMap<String> toMap(String charset);
}