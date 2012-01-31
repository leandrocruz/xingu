package xingu.netty.http;

import org.apache.commons.lang.StringUtils;

public enum Encoding 
{
    IDENTITY("identity"),
    GZIP("gzip", "x-gzip"),
    ZIP("zip"),
    DEFLATE("deflate", "x-deflate"),
    NONE(null);
    
    private String name;
    
    private String alternate;
    
    private Encoding(String name)
    {
        this.name = name;
    }
    
    private Encoding(String name, String alternate)
    {
        this.name = name;
        this.alternate = alternate;
    }

    public static Encoding from(String value)
    {
        if(StringUtils.isEmpty(value))
        {
            return NONE;
        }
        if(IDENTITY.name.equalsIgnoreCase(value))
        {
            return IDENTITY;
        }
        if(GZIP.name.equalsIgnoreCase(value) || GZIP.alternate.equalsIgnoreCase(value))
        {
            return GZIP;
        }
        if(ZIP.name.equalsIgnoreCase(value))
        {
            return ZIP;
        }
        if(DEFLATE.name.equalsIgnoreCase(value) || DEFLATE.alternate.equalsIgnoreCase(value))
        {
            return DEFLATE;
        }
        return NONE;
    }
}
