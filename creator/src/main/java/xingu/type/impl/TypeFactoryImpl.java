package xingu.type.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.lang.BadParameter;
import xingu.type.TypeFactory;

public class TypeFactoryImpl
    implements TypeFactory, Configurable
{
    @Inject
    private Factory factory;

    private Map<String, Class<?>> types;
    
    /* Don't change type config after the service has been initialized */
    private Map<String, Class<?>> clazzCache;
    
    private List<String> avaliablePackages;
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        Configuration[] packages = conf.getChild("packages").getChildren("package");
        avaliablePackages = new ArrayList<String>(packages.length);
        for (Configuration pkg : packages)
        {
            avaliablePackages.add(pkg.getAttribute("name") + ".");
        }
        types = new HashMap<String, Class<?>>();
        clazzCache = new HashMap<String, Class<?>>();
        Configuration[] array = conf.getChild("types").getChildren("type");
        for (Configuration configuration : array)
        {
            String type = configuration.getAttribute("name");
            String className = configuration.getAttribute("class");
            Class<?> clazz = classForName(className);
            if(clazz == null)
            {
                throw new ConfigurationException("Can't load class: "+className);
            }
            types.put(type, clazz);
        }
    }

    @Override
    public String typeOf(Object object)
    {
        Class<?> clazz = object.getClass();
        Set<String> keys = types.keySet();
        for (String key : keys)
        {
            Class<?> c = types.get(key);
            if(c.equals(clazz))
            {
                return key;
            }
        }
        String className = clazz.getName();
        for(String packageName : avaliablePackages)
        {
            if(className.startsWith(packageName))
            {
                return className.substring(packageName.length());
            }
        }
        return className;
    }

    @Override
    public Class<?> classByType(String type)
    {
        Class<?> result = types.get(type);
        if(result != null)
        {
            return result;
        }

        result = clazzCache.get(type);
        if(result != null)
        {
            return result;
        }
        
        result = classForName(type);
        if(result == null)
        {
            result = tryAvailablePackages(type);
            if(result == null)
            {
                throw new BadParameter("Can't find class for: "+type);
            }
        }
        clazzCache.put(type, result);
        return result;
    }

    private Class<?> tryAvailablePackages(String type)
    {
        for(String packageName : avaliablePackages)
        {
            String name = packageName+type;
            Class<?> result = classForName(name);
            if(result != null)
            {
                return result;
            }
        }
        return null;
    }
    private Class<?> classForName(String name)
    {
        try 
        {
            return Class.forName(name);
        } 
        catch (ClassNotFoundException e) 
        {
            //ignored
            return null;
        }
    }

    @Override
    public Object objectByType(String type)
    {
        Class<?> clazz = classByType(type);
        Object result = factory.create(clazz);
        return result;
    }

    @Override
    public Set<String> types()
    {
        return Collections.unmodifiableSet(types.keySet());
    }
}