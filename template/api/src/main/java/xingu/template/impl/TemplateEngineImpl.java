package xingu.template.impl;

import java.io.Writer;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.template.TemplateEngineException;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;

public class TemplateEngineImpl
    extends TemplateEngineSupport
    implements TemplateEngine, Configurable
{
    private Logger log = LoggerFactory.getLogger(TemplateEngineImpl.class);
    
    @Inject
    private Container container;
    
    private TemplateEngine[] engines;
    
    @Override
    protected String getDefaultConfigurationFile()
    {
	    return null;
    }

	@Override
    protected String getDefaultExtension()
    {
		return ".txt";
    }

    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        super.configure(conf);
        Configuration[] delegates = conf.getChild("delegates").getChildren("delegate");
        engines = new TemplateEngine[delegates.length];
        for (int i = 0; i < delegates.length; i++)
        {
            Configuration configuration = delegates[i];
            String key = configuration.getAttribute("key");
            try
            {
                TemplateEngine engine = container.lookup(TemplateEngine.class, key);
                engines[i] = engine;
            }
            catch (Throwable t)
            {
                log.error("Can't find template engine with key: "+key,t);
                engines[i] = NullTemplateEngine.instance();
            }
        }
    }

    private TemplateEngine getEngineForTemplate(String templateName)
        throws TemplateEngineException    
    {
        TemplateEngine result = null;
        for (int i = 0; i < engines.length; i++)
        {
            TemplateEngine engine = engines[i];
            if(engine.templateExists(templateName))
            {
                result = engine;
                break;
            }
        }
        if(result == null)
        {
           throw new TemplateEngineException("TemplateEngine not found for template ["+templateName+"]"); 
        }
        return result;
        
    }
    
    public void merge(String templateName, Context context, Writer writer)
        throws TemplateEngineException
    {
        TemplateEngine engine = getEngineForTemplate(templateName);
        engine.merge(templateName, context, writer);
    }
    
    public boolean templateExists(String templateName)
            throws TemplateEngineException
    {
        for (int i = 0; i < engines.length; i++)
        {
            TemplateEngine engine = engines[i];
            if(engine.templateExists(templateName)) {
                return true;
            }
        }
        return false;
    }
}
