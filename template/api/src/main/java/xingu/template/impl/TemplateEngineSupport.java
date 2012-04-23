package xingu.template.impl;

import java.net.URL;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.template.TemplateEngineException;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Environment;

public abstract class TemplateEngineSupport
	implements TemplateEngine, Configurable
{
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected String extension;
    
    protected String encoding;
    
    protected String onErrorTemplate;
    
    protected URL configurationFile;
    
    @Inject
    protected Environment env;
    
    public void configure(Configuration conf)
		throws ConfigurationException
	{
        extension = conf.getChild("templates").getAttribute("extension",getDefaultExtension());
        encoding = conf.getChild("encoding").getAttribute("name","ISO-8859-1");
        onErrorTemplate = conf.getChild("templates").getChild("onError").getAttribute("template","screens.Error");
        String fileName = conf.getChild("file").getAttribute("name",getDefaultConfigurationFile());
        if(fileName != null)
        {
            configurationFile = Thread.currentThread().getContextClassLoader().getResource(fileName);
        }
        log.info("TemplateEngine configuration file ["+configurationFile+"]");
	}

    protected abstract String getDefaultExtension();
    
    protected abstract String getDefaultConfigurationFile();
    
    
    public Context createContext()
		throws TemplateEngineException
	{
        return new ContextImpl(20);
	}
    
    public String toFileName(String templateName)
    {
        if(templateName.endsWith(extension))
        {
            int pos = templateName.length() - extension.length();
            templateName = templateName.substring(0,pos);
        }
        String tmp = templateName.replaceAll("\\.","/");
        return tmp + extension;
    }

    public String toTemplateName(String templateName)
    {
        if(templateName.startsWith("/"))
        {
            templateName = templateName.substring(1);
        }
        if(templateName.endsWith(extension))
        {
            int pos = templateName.length() - extension.length();
            templateName = templateName.substring(0,pos);
        }
        String tmp = templateName.replaceAll("/","\\.");
        return tmp;
    }
    
    public String getTemplateExtension()
    {
        return extension;
    }

    public String getEncoding()
    {
        return encoding;
    }
    
    public String getOnErrorTemplate()
    {
    	return onErrorTemplate;
    }

	public String getRawText(String templateName)
    {
	    return "getRawText() NOT AVAILABLE";
    }
}
