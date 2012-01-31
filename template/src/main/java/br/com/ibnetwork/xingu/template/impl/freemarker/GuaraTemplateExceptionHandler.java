package br.com.ibnetwork.xingu.template.impl.freemarker;

import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class GuaraTemplateExceptionHandler
    implements TemplateExceptionHandler
{
    public void handleTemplateException(TemplateException e, Environment env, Writer writer) 
        throws TemplateException
    {
    	throw e;
    }

}
