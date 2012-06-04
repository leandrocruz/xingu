package xingu.url.impl;

import xingu.url.DomainName;

public class DomainNameImpl
    implements DomainName
{
    private static final String GENERIC_TOP_LEVEL_DOMAINS = "|aero|arpa|asia|biz|cat|com|coop|edu|gov|imb|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx|";
    
    private String fullName;

    private String[] parts;

    private String first;

    private String second;

    private String third;

    private int size;
    
    DomainNameImpl()
    {}
    
    public DomainNameImpl(String fullName)
    {
        this.fullName = fullName;
        parse0(fullName);
    }

    @Override
    public String fullName()
    {
        return fullName;
    }

    private void parse0(String name)
    {
        parts = fullName.split("\\.");
        size = parts.length;
        first = parts[size - 1];
        second = size > 1 ? parts[size - 2] : null;
        third = size > 2 ? parts[size - 3] : null;
    }

    @Override
    public String registeredName()
    {
        String registeredName = second+"."+first;
        if (third != null && isGenericTopLevelDomain(second))
        {
            registeredName = third+"."+registeredName;
        }
        return registeredName;
    }
    
    @Override
    public boolean isTopLevelDomain()
    {
        if(size > 2)
        {
            return false;
        }
        if(size == 1)
        {
            return true;
        }
        String secondPart = parts[size - 2];
        return isGenericTopLevelDomain(secondPart);
    }
    
    private static boolean isGenericTopLevelDomain(String part)
    {
        return GENERIC_TOP_LEVEL_DOMAINS.contains("|"+part+"|");
    }

    @Override
    public boolean isLocalHost()
    {
        return size == 1 && "localhost".equals(first);
    }
}