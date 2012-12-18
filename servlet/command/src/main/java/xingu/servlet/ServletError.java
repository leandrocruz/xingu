package xingu.servlet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.ibnetwork.xingu.utils.FieldUtils;

public class ServletError
{
    protected String result = "ERROR"; //same as ErrorReply so we can use the same properties on client side javascript (JSON)
    
    protected String reason; //same as ErrorReply so we can use the same properties on client side javascript (JSON) 
    
    protected String stackTrace;
    
    protected String objectName;

    protected Map<String, String> properties = new HashMap<String, String>();
    
    public ServletError()
    {}
    
    public ServletError(Throwable t)
    {
        if(t != null)
        {
            stackTrace = ExceptionUtils.getStackTrace(t);
        }
    }
    
    public ServletError(Object obj, Throwable t)
    {
    	populate(obj);
    	if(t != null)
    	{
    	    reason = t.getMessage();
    	    stackTrace = ExceptionUtils.getStackTrace(t);
    	}
    }

    public ServletError(Object obj, String errorMessage)
    {
        populate(obj);
        this.reason = errorMessage;
    }
    
    private void populate(Object obj)
    {
        if(obj != null)
        {
            this.objectName = obj.getClass().getName();
            try
            {
                List<Field> fields = FieldUtils.getAllFields(obj.getClass());
                for (Field field : fields)
                {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if(value != null)
                    {
                        properties.put(field.getName(), value.toString());
                    }
                    field.setAccessible(accessible);
                }
            }
            catch (Throwable t)
            {
                //ignored
            }
        }
        else
        {
            this.objectName = "NULL"; 
        }
    }

    public String reason()
    {
        return reason;
    }

    public String stackTrace()
    {
        return stackTrace;
    }

    public String objectName()
    {
        return objectName;
    }

    public Map<String, String> properties()
    {
        return properties;
    }

    @Override
    public String toString()
    {
        return objectName + " errorMessage('"+reason+"')";
    }
}
