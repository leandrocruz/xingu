package xingu.template;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SampleMethod
    implements TemplateMethodModelEx
{

    public Object exec(List list) 
        throws TemplateModelException
    {
        SimpleScalar param = (SimpleScalar) list.get(0);
        return param.getAsString().toUpperCase(); 
    }

}
