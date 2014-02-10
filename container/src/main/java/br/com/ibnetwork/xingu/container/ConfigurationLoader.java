package br.com.ibnetwork.xingu.container;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

import br.com.ibnetwork.xingu.utils.FSUtils;

public class ConfigurationLoader
{
    private static final DefaultConfigurationBuilder builder = new  DefaultConfigurationBuilder();
    
    private static final Pattern MASK = Pattern.compile("^(\\w+)\\.xml$");

    public static Configuration load(InputStream is)
    	throws Exception
    {
		return builder.build(is);
    }
    
	public static Configuration load(File file)
		throws Exception
	{
		if(!file.exists())
		{
			throw new Exception("Can't find container file: " + file
					+ ". Please check your classpath or set the "
					+ "XINGU_CONTAINER_FILE environment variable");
		}
		return builder.buildFromFile(file);
	}

    public static Configuration loadNew(String fileName) 
    	throws Exception
    {
    	File file = new File(fileName);
    	if(!file.exists())
    	{
    		String name = FSUtils.loadFromClasspath(fileName);
    		file = new File(name);
    	}

    	if(file == null || !file.exists())
    	{
    		throw new Exception("Can't load configuration from file '"+fileName+"' " + file);
    	}
    	
    	Configuration root = builder.buildFromFile(file);
		Matcher m = MASK.matcher(file.getName());
		if(!m.matches())
		{
			return root;
		}
		
		String base = m.group(1);
		Pattern pattern = Pattern.compile("^"+base+"\\.(\\d+)\\.xml$");
		List<File> complements = findComplements(file, pattern);
		for (File complement : complements)
		{
			Configuration c = builder.buildFromFile(complement);
			root = merge(root, c);
		}
		return root;
    }

	private static Configuration merge(Configuration main, Configuration complement)
		throws ConfigurationException
	{
		Configuration[] children = main.getChildren("component");
		for (Configuration child : children)
		{
            String role = child.getAttribute("role");
            String key = child.getAttribute("key", null);
            Configuration replacement = findReplacementFor(role, key, complement);
            if(replacement != null)
            {
            	((DefaultConfiguration) main).removeChild(child);
            	((DefaultConfiguration) main).addChild(replacement);
            }
		}
		return main;
	}

	private static Configuration findReplacementFor(String role, String key, Configuration conf)
		throws ConfigurationException
	{
		System.out.println(role + ":" +key);
		Configuration[] children = conf.getChildren("component");
		for (Configuration replacement : children)
		{
            String r = replacement.getAttribute("role");
            String k = replacement.getAttribute("key", null);
            if(role.equals(r) && (key == null ? k == null : key.equals(k)))
            {
            	return replacement;
            }
		}
		return null;
	}

	private static List<File> findComplements(File pulga, final Pattern pattern)
	{
		File directory = pulga.getParentFile();
		File[] files = directory.listFiles();
		List<File> result = new ArrayList<File>();
		for (File f : files)
		{
			if(!f.isDirectory())
			{
				int position = indexOf(f, pattern);
				if(position >= 0)
				{
					result.add(f);
				}
			}
		}
		
		Collections.sort(result, new Comparator<File>(){

			@Override
			public int compare(File f1, File f2)
			{
				int p1 = indexOf(f1, pattern);
				int p2 = indexOf(f2, pattern);
				return p1 - p2;
			}
		});
		
		return result;
	}

	private static int indexOf(File file, Pattern pattern)
	{
		String name = file.getName();
		Matcher m = pattern.matcher(name);
		if(m.matches())
		{
			String index = m.group(1);
			return Integer.parseInt(index);
		}
		return -1;
	}
}
