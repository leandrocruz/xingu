package xingu.maze.impl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import xingu.maze.Domain;

public class DomainImpl
    extends URLClassLoader 
    implements Domain
{
    private String name;
    
    private List<String> resources = new ArrayList<String>();

    private static final URL[] EMPTY = new URL[]{};
    
    DomainImpl(String name)
    {
        super(EMPTY);
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public ClassLoader classLoader()
    {
        return this;
    }
    
    @Override
    public String toString()
    {
        return "Domain ["+name+"]";
    }

    void load(String resource)
        throws Exception
    {
        if(!resource.endsWith(".jar") && !resource.endsWith("/"))
        {
            resource += "/";
        }
        File file = new File(resource);
        addURL(file.toURI().toURL());
        resources.add(resource);
    }

    @Override
    public List<String> entries()
    {
        return resources;
    }
}
