package xingu.template.impl.freemarker;

import freemarker.cache.CacheStorage;

public class NullCacheStorage
    implements CacheStorage
{

    public Object get(Object arg0)
    {
        return null;
    }

    public void put(Object arg0, Object arg1)
    {
    }

    public void remove(Object arg0)
    {
    }

    public void clear()
    {
    }
}
