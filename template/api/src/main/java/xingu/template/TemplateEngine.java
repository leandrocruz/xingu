package xingu.template;

import java.io.Writer;

public interface TemplateEngine
{
    void merge(String templateName, Context context, Writer writer)
    	throws TemplateEngineException;
   
    boolean templateExists(String templateName)
    	throws TemplateEngineException;

    //TODO: reclycle Context objects
    Context createContext()
    	throws TemplateEngineException;
    
    String getTemplateExtension();
    
    String getEncoding();
    
    String getOnErrorTemplate();
    
    String toTemplateName(String templateName);
    
    String toFileName(String templateName);
    
    String getRawText(String templateName)
    	throws TemplateEngineException;
}
