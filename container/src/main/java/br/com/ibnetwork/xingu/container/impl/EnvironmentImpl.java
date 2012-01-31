package br.com.ibnetwork.xingu.container.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;

import br.com.ibnetwork.xingu.container.Environment;

public class EnvironmentImpl
    implements Environment, Initializable
{
	/*
	 * Matches ${x.y}
	 */
	private static final Pattern pattern = Pattern.compile("\\$\\{([\\w\\.\\?\\/\\\\: ]+)\\}");
	
	private Map<String, String> map = new HashMap<String, String>();

	@Override
    public void initialize() 
	    throws Exception
    {
        String userDir = SystemUtils.getUserDir().getAbsolutePath();
        map.put("user.dir", userDir);
        map.put("user.home", SystemUtils.getUserHome().getAbsolutePath());
        map.put("tmp.dir", SystemUtils.getJavaIoTmpDir().getAbsolutePath());
        map.put("${app.root}", userDir);
        map.put("${pom.build.testOutputDirectory}", userDir);
    }
	
	public void put(String key, String value)
	{
		this.map.put(key, value);
	}
	
	public String get(String key)
	{
		return this.map.get(key);
	}
	
    /*
     * Replaces framework configuration constants by values:
     * 
     * <p>
     * Mapped by Environment
     * ${app.root} = application root
     * </p>
     * 
     * <p>
     * No Mapping
     * ${foo} = ${foo}
     * </p>
     * 
     * <p>
     * You can use System properties as well
     * ${user.dir} = /home/...
     * </p>
     * 
     * @see http://www.regular-expressions.info/java.html 
     */
    public String replaceVars(String var)
    {
	    if(var == null)
	    {
	    	return null;
	    }
	    Matcher m = pattern.matcher(var);
	    StringBuffer sb = new StringBuffer();
	    while (m.find()) 
	    {
	    	String group = m.group(); // with ${}
	    	String propertyName = m.group(1); // without ${}
	    	String replacement = map.get(group); 
	    	String defaultValue = null;
	    	int index;
	    	if((index = propertyName.indexOf("?")) > 0)
	    	{
	    	    defaultValue = propertyName.substring(index + 1);
	    	    propertyName = propertyName.substring(0, index);
	    	}
	    	
	    	//system property
	    	String property = System.getProperty(propertyName);
	    	if(replacement == null && property != null)
	    	{
	    		replacement = property;
	    	}
            
	    	//environment variable
	    	property = System.getenv(propertyName);
            if(replacement == null && property != null)
            {
                replacement = property;
            }
	    	
            //default value
            if(replacement == null && defaultValue != null)
	    	{
	    	    replacement = defaultValue;
	    	}
	    	
	    	if (replacement != null) 
	    	{
	    		replacement = FilenameUtils.separatorsToUnix(replacement); //XXX: needed on windows
	    		m.appendReplacement(sb, replacement);
	    	}
	    }
	    m.appendTail(sb);
	    return sb.toString();
    }

	public void replaceVars(Properties props)
    {
    	for (Object element : props.keySet())
        {
    		String key = (String) element;
    		String value = props.getProperty(key);
    		value = replaceVars(value);
    		props.put(key, value);
    	}
    }
}
