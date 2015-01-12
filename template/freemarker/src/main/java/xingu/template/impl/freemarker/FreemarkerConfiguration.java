package xingu.template.impl.freemarker;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Environment;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.utils.ObjectUtils;
import freemarker.cache.CacheStorage;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateTransformModel;

public class FreemarkerConfiguration
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private Factory factory;
    
    @Inject
    private Environment env;
    
    private Map<String, TemplateMethodModel> methods = new HashMap<String, TemplateMethodModel>();
    
    private Map<String, TemplateTransformModel> transforms = new HashMap<String, TemplateTransformModel>();
    
    private List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>(5);
    
    public void configureTemplateEngine(URL url,Configuration cfg)
        throws Exception
    {
        log.debug("Loading freemarker configuration from: "+url);
        Properties props = new Properties();
        props.load(url.openStream());
        env.replaceVars(props);
        for (Iterator iter = props.keySet().iterator(); iter.hasNext();)
        {
            String key = (String) iter.next();
            String value = StringUtils.trimToNull(props.getProperty(key));
            if(value != null)
            {
                updateEntry(cfg, key,value);
            }
        }
        
        //template loaders
        TemplateLoader loader;
        if(templateLoaders.size() == 0)
        {
        	throw new Exception("No template loaders defined");
        }
        else
        {
            loader = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[0]));
        }
        cfg.setTemplateLoader(loader); 
    }

	private void updateEntry(Configuration cfg, String key, String value)
        throws Exception
    {
        log.debug("Configuration key["+key+"] value["+value+"]");
        if("cacheStorage".equalsIgnoreCase(key))
        {
            CacheStorage obj = (CacheStorage) createObject(value);
            cfg.setCacheStorage(obj);
        }
        else if("defaultEncoding".equalsIgnoreCase(key))
        {
            cfg.setDefaultEncoding(value);
        }
        else if("strictSyntaxMode".equalsIgnoreCase(key))
        {
            cfg.setStrictSyntaxMode(new Boolean(value).booleanValue());
        }
        else if("whitespaceStripping".equalsIgnoreCase(key))
        {
            cfg.setWhitespaceStripping(new Boolean(value).booleanValue());
        }
        else if("exceptionHandler".equalsIgnoreCase(key))
        {
            TemplateExceptionHandler handler = (TemplateExceptionHandler) createObject(value);
            cfg.setTemplateExceptionHandler(handler);
        }
        else if(key.startsWith("autoImport"))
        {
        	String nameSpace = key.substring(key.indexOf(".") + 1); 
        	cfg.addAutoImport(nameSpace,value);
        }
        else if(key.startsWith("method"))
        {
            String name = key.substring(key.indexOf(".") + 1); 
            TemplateMethodModel method = (TemplateMethodModel) ObjectUtils.getInstance(value);
            methods.put(name,method);
        }
        else if(key.startsWith("transform"))
        {
            String name = key.substring(key.indexOf(".") + 1); 
            TemplateTransformModel transform = (TemplateTransformModel) ObjectUtils.getInstance(value);
            transforms.put(name,transform);
        }
        else if(key.startsWith("loader"))
        {
        	//collect template loaders
        	String type = key.substring("loader".length() + 1);
        	TemplateLoader loader = null;
        	if(type.equalsIgnoreCase("file.directoryForTemplateLoading"))
        	{
        		loader = new FileTemplateLoader(new File(value));
        	}
        	else if(type.equalsIgnoreCase("classPath") && value.equalsIgnoreCase("enabled"))
        	{
        		loader = new ClassTemplateLoader(getClass(), "/");
        	}
        	if(loader != null)
        	{
        		templateLoaders.add(loader);
        	}
        }

        else
        {
            cfg.setSetting(key,value);
        }
    }

    private Object createObject(String className)
    {
		Class<?> clazz = ObjectUtils.loadClass(className);
		return factory.create(clazz);
    }

    public Map<String, TemplateMethodModel> getMethods()
    {
        return methods;
    }

    public Map<String, TemplateTransformModel> getTransforms()
    {
        return transforms;
    }
}
