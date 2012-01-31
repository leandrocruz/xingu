package br.com.ibnetwork.xingu.container.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Binding;

public class SimpleBinder
    implements Binder
{
    private Map<String, Binding<?>> map = new HashMap<String, Binding<?>>();

    @Override
    public <T> Binding<T> get(Class<T> role)
    {
        return get(role, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Binding<T> get(Class<T> role, String key)
    {
        String roleName = role.getName();
        if(!StringUtils.isEmpty(key))
        {
            roleName += ":" + key;
        }
        Binding<?> ref = map.get(roleName); 
        return (Binding<T>) ref;
    }

    @Override
    public <T> Binding<T> bind(Class<T> role)
    {
        return bind(role, null);
    }

    @Override
    public <T> Binding<T> bind(Class<T> role, String key)
    {
        Binding<T> binding = new SimpleBinding<T>(role);
        String roleName = role.getName(); 
        if(!StringUtils.isEmpty(key))
        {
            roleName += ":" + key;
        }
        map.put(roleName, binding);
        return binding;
    }

 
}

class SimpleBinding<T>
    implements Binding<T>
{
    private T impl;
    
    private Class<T> role;
    
    
    private boolean ready;

    private Class<? extends T> implClass;

    public SimpleBinding(Class<T> role)
    {
        this.role = role;
    }

    @Override
    public boolean isReady()
    {
        return ready;
    }

    @Override
    public void isReady(boolean ready)
    {
        this.ready = ready;
    }

    @Override
    public Class<T> role()
    {
        return role;
    }
    
    @Override
    public T impl()
    {
        return impl;
    }

    @Override
    public <E extends T> void to(E impl)
    {
        this.impl = impl;
    }

    @Override
    public String toString()
    {
        if(impl != null)
        {
            return "to ["+impl.getClass().getName()+"]";
        }
        else
        {
            return "to [NULL]";
        }
    }

    @Override
    public <E extends T> void to(Class<E> implClass)
    {
        this.implClass = implClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends T> Class<E> implClass()
    {
        return (Class<E>) implClass;
    }
}