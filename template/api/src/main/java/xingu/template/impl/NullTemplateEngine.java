package xingu.template.impl;

import java.io.Writer;

import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.template.TemplateEngineException;

public class NullTemplateEngine
    implements TemplateEngine
{
    private static final TemplateEngine INSTANCE = new NullTemplateEngine();
    
    private NullTemplateEngine() {}
    
    public static TemplateEngine instance()
    {
        return INSTANCE;
    }

    public void merge(String templateName, Context context, Writer writer)
            throws TemplateEngineException
    {
    }

    public boolean templateExists(String templateName)
            throws TemplateEngineException
    {
        return false;
    }

    public Context createContext() 
        throws TemplateEngineException
    {
        return null;
    }

    public String getTemplateExtension()
    {
        return null;
    }

    public String getEncoding()
    {
        return null;
    }

    public String getOnErrorTemplate()
    {
        return null;
    }

    public String toTemplateName(String templateName)
    {
        return null;
    }

    public String toFileName(String templateName)
    {
        return null;
    }

	public String getRawText(String templateName)
    {
	    return null;
    }
}
