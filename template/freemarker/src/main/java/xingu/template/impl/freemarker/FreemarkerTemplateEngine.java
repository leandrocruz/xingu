package xingu.template.impl.freemarker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import xingu.template.Context;
import xingu.template.TemplateEngineException;
import xingu.template.impl.TemplateEngineSupport;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import freemarker.cache.TemplateLoader;

import freemarker.log.Logger;
import freemarker.core.InvalidReferenceException;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateTransformModel;

public class FreemarkerTemplateEngine
	extends TemplateEngineSupport
	implements Configurable
{
    private freemarker.template.Configuration cfg;
    
    /*
     * There is no way to get the line/column information from freemarker
     * when a InvalidReferenceException is thrown. So whe try our best to extract this 
     * information from the error message
     * Example message:
     * 
     * Expression xx is undefined on line 2, column 11 in screens/Test.ftl.
     */
    public static final Pattern invalidReference = Pattern.compile("Expression (.+) is undefined on line (\\d+), column (\\d+).*");
    
	/*
	 * Error on line xx, column yy in xzy
	 * Error on line 69, column 75 in screens/sample/Ajax.ftl 
	 * Expecting a string, date or number here, Expression sample.ativo! is instead a freemarker.template.TemplateBooleanModel$2
	 */
    public static final Pattern global =  Pattern.compile(".*on line (\\d+), column (\\d+)[\\w\\W]* Expression (.+) is .*");
    
    @Inject
    private Factory factory;

    private FreemarkerConfiguration wrapper;
    
	@Override
    protected String getDefaultConfigurationFile()
    {
	    return "freemarker.properties";
    }

	@Override
    protected String getDefaultExtension()
    {
		return ".ftl";
    }

	public void configure(Configuration conf)
    	throws ConfigurationException
    {
		super.configure(conf);
        try
		{
			Logger.selectLoggerLibrary(Logger.LIBRARY_SLF4J);
		}
		catch (ClassNotFoundException e)
		{
			throw new ConfigurationException("Error loading loger library", e);
		}
        
        wrapper = (FreemarkerConfiguration) factory.create(FreemarkerConfiguration.class);
        cfg = new freemarker.template.Configuration();
        try
        {
            wrapper.configureTemplateEngine(configurationFile,cfg);
        }
        catch (Exception e)
        {
            throw new ConfigurationException("Error on freemarker configuration",e);
        }
    }

    public void merge(String templateName, Context context, Writer w)
    	throws TemplateEngineException
    {
        String realName = toFileName(templateName);
        if(log.isDebugEnabled()) log.debug("rendering [" + realName + "]");
        try
        {
            Template template = cfg.getTemplate(realName);
            addMethods(context, wrapper.getMethods());
            addTransforms(context, wrapper.getTransforms());
            template.process(context.getMap(),w);
        }
        catch (Throwable t)
        {
        	throw createTemplateException("Error merging template ["+realName+"]", t, templateName);
        }
    }

    private void addMethods(Context context, Map<String, TemplateMethodModel> map)
    {
        for(Iterator<String> it = map.keySet().iterator(); it.hasNext();)
        {
            String name = it.next();
            TemplateMethodModel obj = map.get(name);
            context.put(name,obj);
        }
    }
    
    private void addTransforms(Context context, Map<String, TemplateTransformModel> map)
    {
        for(Iterator<String> it = map.keySet().iterator(); it.hasNext();)
        {
            String name = it.next();
            TemplateTransformModel obj = map.get(name);
            context.put(name,obj);
        }
    }

    public boolean templateExists(String templateName)
    	throws TemplateEngineException
    {
        String realName = toFileName(templateName);
        Template template;
        try
        {
            template = cfg.getTemplate(realName);
            return template != null;
        }
        catch (Throwable t)
        {
        	if(t instanceof ParseException)
        	{
        		throw createTemplateException("Error parsing template ["+realName+"]", t, templateName);
        	}
            /*
             * Ignored. 
             * Freemarker doesn't provide any API call to determine if a template
             * exists or not without trying to parse it
             */
        	log.debug("Can't find template: "+realName+". "+t.getMessage());
            return false;
        }
    }
    
	private TemplateEngineException createTemplateException(String message, Throwable t, String templateName)
    {
	    TemplateEngineException tee = new TemplateEngineException(message,t); 
	    tee.setTemplateName(templateName);
	    tee.setFileName(toFileName(templateName));
	    if(t instanceof InvalidReferenceException)
	    {
	    	InvalidReferenceException error = (InvalidReferenceException) t;
	    	String s = error.getMessage();
	    	Matcher m = invalidReference.matcher(s);
	    	if(m.matches())
	    	{
	    		tee.setExpression(m.group(1));
		    	tee.setLineNumber(Integer.parseInt(m.group(2)));
		        tee.setColumnNumber(Integer.parseInt(m.group(3)));
	    	}
	    }
	    else if(t instanceof ParseException)
	    {
	    	ParseException error = (ParseException) t;
	        tee.setLineNumber(error.getLineNumber());
	        tee.setColumnNumber(error.getColumnNumber());
	    }
	    else if(t instanceof TemplateException)
	    {
	    	TemplateException error = (TemplateException) t;
	    	String s = error.getMessage();
	    	Matcher m = global.matcher(s);
	    	if(m.matches())
	    	{
	    		tee.setExpression(m.group(3));
		    	tee.setLineNumber(Integer.parseInt(m.group(1)));
		        tee.setColumnNumber(Integer.parseInt(m.group(2)));
	    	}
	    }
	    return tee;
    }

	@Override
    public String getRawText(String templateName)
    	throws TemplateEngineException
	{
		try
        {
			TemplateLoader loader = cfg.getTemplateLoader();
			Object source = loader.findTemplateSource(toFileName(templateName));
			if(source != null)
			{
				Reader reader = loader.getReader(source, getEncoding());
		        String result = readFromReader(reader, source);
		        return result;
			}
			return null;
        }
        catch (IOException e)
        {
	        throw new TemplateEngineException("Error getting raw text for template ["+templateName+"]",e);
        }
    }

	private String readFromReader(Reader reader, Object source) 
		throws IOException
    {
	    StringBuffer sb = new StringBuffer(1000);
	    reader = new BufferedReader(reader);
	    char[] buf = new char[1024];
	    int numRead=0;
	    while((numRead=reader.read(buf)) != -1)
	    {
	        String readData = String.valueOf(buf, 0, numRead);
	        sb.append(readData);
	        buf = new char[1024];
	    }
	    reader.close();
	    return sb.toString();
    }
}
