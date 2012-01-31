package br.com.ibnetwork.xingu.template;

import java.io.Writer;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public interface TemplateEngine
{
    String ROLE = TemplateEngine.class.getName();
    
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
