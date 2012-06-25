package xingu.url;

import java.io.Serializable;

public interface Url
    extends Serializable
{
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

	int pathLevel();

	int domainLevel();

	String getDomainAndPath();

	boolean isSameDomainAndPath(Url other);

	boolean appliesTo(Url url);

	boolean isValid();

	String getExtension();
}
