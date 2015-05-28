package xingu.maze.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xingu.maze.Config;
import xingu.maze.ConfigParser;
import xingu.maze.Utils;

public class ConfigParserImpl implements ConfigParser
{
    private InputStream is;
    
    private Map<String, String> vars = new HashMap<String, String>();

    private DomainImpl currentDomain;

    private ConfigImpl config;
    
    public ConfigParserImpl(InputStream is)
    {
        this.is = is;
    }
    
    public Config parse()
        throws Exception
    {
        config = new ConfigImpl(vars);
        List<String> lines = Utils.readLines(is);
        for (String line : lines)
        {
            parseLine(line);
        }
        return config;
    }

    private void parseLine(String line)
        throws Exception
    {
        //System.out.println("> "+line);
        if(line.indexOf("=") > 0)
        {
            int index = line.indexOf("=");
            String key = line.substring(0, index);
            String value = line.substring(index+1);
            value = value.trim();
            value = Interpolator.interpolate(value);
            vars.put(key.trim(), value.trim());
        }
        else if(line.startsWith("["))
        {
            int index = line.indexOf("]");
            String name = line.substring(1, index);
            currentDomain = new DomainImpl(name);
            config.add(currentDomain);
        }
        else if(line.startsWith("load"))
        {
            String resource = line.substring("load".length() + 1); 
            resource = Interpolator.interpolate(resource, vars);
            int idx = resource.indexOf("*"); 
            if(idx >= 0)
            {
                File baseDir = new File(resource.substring(0, idx));
                String extension = resource.substring(idx + 1);
                File[] filtered = baseDir.listFiles(new SuffixFileFilter(extension));
                if(filtered != null)
                {
                    for (File file : filtered)
                    {
                        //System.out.println("\t"+file);
                        currentDomain.load(file.getAbsolutePath());
                    }
                }
            }
            else
            {
                currentDomain.load(resource);
            }
        }
    }
}

class SuffixFileFilter
    implements FilenameFilter
{
    private String suffix;
    
    public SuffixFileFilter(String suffix)
    {
        this.suffix = suffix;
    }
    
    @Override
    public boolean accept(File dir, String name)
    {
        return name.endsWith(suffix);
    }
}