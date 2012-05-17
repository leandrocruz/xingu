package xingu.url;

import java.io.Serializable;

public interface Url
    extends Serializable
{
    boolean isValid();

    String getScheme();

    String getHost();

    DomainName getDomainName();

    int getPort();

    String getPath();

    String getFragment();
    
    String getSpec(); //TODO: remove this method after UpdateSimpleUrl is run
    
    String getQuery();
    QueryString getQueryString();

    boolean hasParam(String name);
    
    boolean hasParam(String name, String value);

    Url addParam(String name, String value);

    boolean isFavIcon();
    
    boolean isIp();
}