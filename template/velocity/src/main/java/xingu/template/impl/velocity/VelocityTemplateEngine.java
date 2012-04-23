package xingu.template.impl.velocity;

//AVALON
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

//VELOCITY
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import xingu.template.Context;
import xingu.template.TemplateEngine;
import xingu.template.TemplateEngineException;
import xingu.template.impl.TemplateEngineSupport;

//XINGU

//JDK
import java.io.Writer;
import java.util.Properties;

public class VelocityTemplateEngine
    extends TemplateEngineSupport
	implements TemplateEngine, Configurable
{
	private VelocityEngine ve;
	
	@Override
    protected String getDefaultConfigurationFile()
    {
	    return "velocity.properties";
    }

	@Override
    protected String getDefaultExtension()
    {
		return ".vm";
    }

	public void configure(Configuration conf) 
		throws ConfigurationException
	{
		super.configure(conf);
        log.debug("Loading velocity configuration from: "+configurationFile);
        ve = new VelocityEngine();
        try
        {
			Properties props = new Properties();
			props.load(configurationFile.openStream());
			env.replaceVars(props);
		    ve.init(props);
        }
        catch (Exception e)
        {
            throw new ConfigurationException("Error configuring VelocityTemplateEngine",e);
        }
	}

    public boolean templateExists(String templateName) 
    	throws TemplateEngineException
    {
        String realName = toFileName(templateName);
        return ve.templateExists(realName);
    }

    public void merge(String templateName, Context context, Writer w)
    	throws TemplateEngineException
    {
        String realName = toFileName(templateName);
        if(log.isDebugEnabled()) log.debug("rendering [" + realName + "]");		
		VelocityContext ctx = switchContext(context);
    	try
        {
    	    Template template = ve.getTemplate(realName,encoding);
    	    template.merge(ctx, w);
        }
        catch (Exception e)
        {            
			throw new TemplateEngineException("Error merging template["+realName+"] ", e);
        }
    }

    private VelocityContext switchContext(Context context)
    {
        return new VelocityContext(context.getMap());
    }
}