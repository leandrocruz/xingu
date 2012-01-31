package br.com.ibnetwork.xingu.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateUtils 
{
    private static Logger logger = LoggerFactory.getLogger(TemplateUtils.class);

    public static String renderTemplate(TemplateEngine engine, String encoding, Context ctx, String templateName)
    	throws TemplateEngineException
    {
        ByteArrayOutputStream bytes = null;
        String result;
        try
        {
            bytes = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(bytes, encoding);
            engine.merge(templateName, ctx, writer);
            writer.flush();
            result = bytes.toString(encoding);
            return result;
        }
        catch (Throwable t)
        {
            throw new TemplateEngineException("Error rendering template ["+templateName+"]",t);
        }
        finally
        {
            try
            {
                if (bytes != null)
                {
                    bytes.close();
                }
            }
            catch (IOException e)
            {
                logger.warn("Error closing output stream",e);
            }
        }
    }
}
