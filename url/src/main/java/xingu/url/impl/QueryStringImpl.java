package xingu.url.impl;

import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import xingu.url.QueryString;
import xingu.utils.StringUtils;
import xingu.utils.collection.FluidMap;

public class QueryStringImpl
    implements QueryString
{
    private static final String REGEX = "^[^=&]+=[^&]*(&[^=&]+=[^&]*)*$";
    
    private String value;
    
    private transient FluidMap<String> map;
    
    public static final QueryString EMPTY = new QueryStringImpl("");
    
    /* HACK: duplicate from HttpUtils.DEFAULT_HTTP_CHARSET */
    public static String DEFAULT_HTTP_CHARSET_NAME = "ISO-8859-1";

    public QueryStringImpl()
    {}
    
    public QueryStringImpl(String queryString)
    {
        this.value = queryString;
    }

    @Override
    public String get(String name)
    {
        String value = map().get(name);
        return value;
    }
	
    public List<String> getAll(String key)
	{
		return map.getAll(key);
	}
	
	@Override
	public Set<String> names()
	{
		return map.keySet();
	}

    @Override
    public String getDecoded(String name)
    {
        return getDecoded(name, DEFAULT_HTTP_CHARSET_NAME);
    }

    @Override
    public String getDecoded(String name, String enc)
    {
        String value = map().get(name);
        String result = decode(value, enc);
        //System.err.println("["+enc+"]\n" + value + " = " + result);
        return result;
    }

    private String decode(String value, String enc)
    {
        if(StringUtils.isEmpty(value))
        {
            return value;
        }
        
        try
        {
            return URLDecoder.decode(value, enc != null ? enc : DEFAULT_HTTP_CHARSET_NAME);
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    @Override
    public int size()
    {
        return map().size();
    }

    @Override
	public FluidMap<String> toMap()
	{
    	return toMap(DEFAULT_HTTP_CHARSET_NAME);
	}
    
    @Override
	public FluidMap<String> toMap(String charset)
	{
		FluidMap<String> map = map().copy();
		Set<String> keys = map.keySet();
		for(String key : keys)
		{
			List<String> values = map.getAll(key);
			for(int i = 0; i < values.size(); i++)
			{
				String value = values.get(i);
				String replacement = decode(value, charset);
				values.set(i, replacement);
			}
		}
		return map;
	}

    private FluidMap<String> map()
    {
        if (map == null)
        {
            map = new FluidMap<String>();
            if(StringUtils.isNotEmpty(value))
            {
                String[] pairs = value.split("&");
                for (String pair : pairs)
                {
                    String[] split = pair.split("=");
                    String key = split[0];
                    String value = split.length > 1 ? split[1] : null;
                    map.add(key, value);
                }
            }
        }
        return map;
    }

    public static boolean isQueryString(String text)
    {
        return text.matches(REGEX);
    }

    private boolean areEqual(String myValue, String otherValue)
    {
        if (myValue == null && otherValue == null)
        {
            return true;
        }
        return otherValue != null ? otherValue.equals(myValue) : false;
    }

    @Override
    public QueryString add(String name, String value)
    {
        map().add(name, value);
        return this;
    }

    @Override
    public String toString()
    {
        if(map == null)
        {
            return value;
        }
        StringBuilder builder = new StringBuilder();
        Set<String> keys = map.keySet();
        int i = 0;
        int size = keys.size();
        for (String key : keys)
        {
            i++;
            String value = map().get(key);
            builder.append(key).append("=").append(value);
            if(i < size)
            {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof QueryString)) return false;
        QueryString other = (QueryString) obj;
        if (other.size() != size()) return false;
        for (String key : map().keySet())
        {
            String myValue = map().get(key);
            String otherValue = other.get(key);
            if (!areEqual(myValue, otherValue))
            {
                return false;
            }
        }
        return true;
    }
}