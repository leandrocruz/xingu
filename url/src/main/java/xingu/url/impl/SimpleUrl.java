package xingu.url.impl;

import xingu.url.UrlUtils;
import xingu.url.DomainName;
import xingu.url.QueryString;
import xingu.url.Url;
import br.com.ibnetwork.xingu.utils.StringUtils;

public class SimpleUrl
    implements Url
{
    private String scheme;
    
    private String host;
    
    private int port;
    
    private String path;
    
    private String query;
    private QueryString queryString;

    private String fragment;

    private DomainName domainName;

    private String spec;

    SimpleUrl()
    {}

    public SimpleUrl(String spec /*original value*/, String scheme, String host, int port, String path, String query, String fragment)
    {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        this.spec = spec;
    }

    @Override
    public boolean isValid()
    {
        return schemeIsValid() && hostIsValid() && portIsValid();
    }

    private boolean portIsValid()
    {
        return port > 0 || port == -1;
    }

    private boolean hostIsValid()
    {
        if(StringUtils.isEmpty(host))
        {
            return false;
        }
        if(isIp())
        {
            return true;
        }
        if("localhost".equals(host))
        {
            return true;
        }
        return UrlUtils.isDomainValid(host);
    }

    private boolean schemeIsValid()
    {
        if(scheme == null)
        {
            return false;
        }
        return "http".equals(scheme) || "https".equals(scheme);
    }

    @Override
    public String getScheme()
    {
        return scheme;
    }

    @Override
    public String getHost()
    {
        return host;
    }

    @Override
    public DomainName getDomainName()
    {
        if(host == null)
        {
            return null;
        }
        if(domainName == null && !isIp())
        {
            domainName = new DomainNameImpl(host);
        }
        return domainName;
    }

    @Override
    public int getPort()
    {
        return port;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public String getQuery()
    {
        return query;
    }
    
    @Override
    public QueryString getQueryString()
    {
        if(queryString != null)
        {
            return queryString;
        }
        
        if(StringUtils.isEmpty(query))
        {
            return null;
        }
        
        queryString = new QueryStringImpl(query);
        return queryString;
    }
    
    private String value(String name)
    {
        QueryString q = getQueryString();
        if(q == null)
        {
            return null;
        }
        return q.get(name);
    }

    @Override
    public boolean hasParam(String name)
    {
        return value(name) != null;
    }

    @Override
    public boolean hasParam(String name, String value)
    {
        String v = value(name);
        return v == null ? value == null : v.equals(value);
    }

    @Override
    public Url addParam(String name, String value)
    {
        QueryString q = getQueryString();
        if(q == null)
        {
            queryString = new QueryStringImpl();
            q = queryString;
        }
        q.add(name, value);
        return this;
    }

    @Override
    public String getFragment()
    {
        return fragment;
    }

    @Override
    public boolean isFavIcon()
    {
        return /* query == null 
                && fragment == null 
                && */ "/favicon.ico".equalsIgnoreCase(path);
    }

    @Override
    public boolean isIp()
    {
        if(host == null)
        {
            return false;
        }
        String[] parts = host.split("\\.");
        if(parts.length != 4)
        {
            return false;
        }
        try
        {
            for(String part : parts)
            {
                Integer.parseInt(part);
            }
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(getScheme()).append("://").append(getHost());
        
        if(port > 0)
        {
            sb.append(":").append(port);
        }
        
        String path = getPath();
        if(path != null && !"/".equals(path))
        {
            sb.append(path);
        }
        
        QueryString q = getQueryString();
        if(q != null)
        {
            String value = q.toString();
            if(value != null)
            {
                sb.append("?");
                sb.append(value);
            }
        }

        String fragment = getFragment();
        if(fragment != null)
        {
            sb.append("#");
            sb.append(fragment);
        }
        
        String result = sb.toString();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Url)) return false;
        Url other = (Url) obj;
        return safeEquals(scheme, other.getScheme())
                && safeEquals(host, other.getHost())
                && port == other.getPort()
                && safeEquals(path, other.getPath())
                && safeEquals(query, other.getQuery())
                && safeEquals(fragment, other.getFragment());
    }

    private boolean safeEquals(String a, String b)
    {
        return a == null ? b == null : a.equals(b);
    }
    
    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public String getSpec()
    {
        //TODO: remove this method after UpdateSimpleUrl is run
        return spec;
    }
}