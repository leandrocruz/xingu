package xingu.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.codec.Codec;
import xingu.container.Container;
import xingu.container.ContainerUtils;
import xingu.container.Inject;
import xingu.lang.NotImplementedYet;
import xingu.servlet.command.BehaviorResolver;
import xingu.servlet.command.Command;
import xingu.servlet.command.CommandBehavior;
import xingu.servlet.command.CommandReply;
import xingu.servlet.command.reply.StringReply;
import xingu.type.TypeFactory;
import xingu.utils.FieldUtils;

public class XinguServlet
    extends HttpServlet
{
    @Inject
    protected BehaviorResolver resolver;
    
    @Inject
    protected TypeFactory typeFactory;
    
    @Inject("xingu.servlet")
    protected Codec codec;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String DEFAULT_REPLY = "Bang";

    @Override
    public void init(ServletConfig config) 
        throws ServletException
    {
        try
        {
            Container container = ContainerUtils.getContainer();
            container.startLifecycle(this, null);
        }
        catch (Exception e)
        {
            throw new ServletException("Error initializing service", e);
        }
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        String responseType = responseType(req);
        res.setContentType(responseType);
        res.setStatus(200);
        
        Object reply = null;
        Object obj = null;
        try
        {
            obj = toObject(req, res);
            if(obj != null)
            {
                reply = process(obj);
            }
        }
        catch (Throwable t)
        {
            logger.error("Error handling HTTP request for: "+obj, t);
            res.setStatus(500);
            reply = errorReply(obj, t);
        }
        
        if(reply == null)
        {
            reply = defaultReply();
        }
        String result = encode(reply);
        IOUtils.write(result, res.getOutputStream());
    }
    
    protected Object process(Object obj)
        throws Exception
    {
        if (obj instanceof Command)
        {
            Command cmd = (Command) obj;
            CommandBehavior behavior = resolver.behaviorFor(cmd);
            CommandReply reply = behavior.perform(cmd);
            return reply;
        }
        else
        {
            throw new NotImplementedYet();
        }
    }

    protected Object toObject(HttpServletRequest req, HttpServletResponse res)
        throws Exception
    {
        String path = req.getPathInfo();
        if(StringUtils.isEmpty(path))
        {
            return null;
        }
        String objectName = path.substring(1);
        if("favicon.ico".equals(objectName))
        {
            return null;
        }
        Object result = objectByType(objectName);
        List<Field> fields = FieldUtils.getAllFields(result.getClass());
        for (Field field : fields)
        {
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null)
            {
                continue;
            }
            Class<?> type = field.getType();
            if(HttpServletRequest.class.isAssignableFrom(type))
            {
                FieldUtils.setField(result, field, req);
                continue;
            }
            if(HttpServletResponse.class.isAssignableFrom(type))
            {
                FieldUtils.setField(result, field, res);
                continue;
            }
            
            String name = field.getName();
            String value = req.getParameter(name);
            if(value != null)
            {
                if(!name.equalsIgnoreCase("password") && logger.isDebugEnabled())
                {
                    logger.debug("value from URL: {}.{} = {} ", new Object[]{objectName, name, value});
                }
                Object converted = toObject(value, field.getType());
                FieldUtils.setField(result, field, converted);
            }
        }
        return result;

    }

    protected Object toObject(String value, Class<?> type)
    {
        return ConvertUtils.convert(value, type);
    }

    protected Object errorReply(Object obj, Throwable t)
    {
        return new ServletError(obj, t);
    }

    protected Object defaultReply()
    {
        return DEFAULT_REPLY;
    }

    protected String responseType(HttpServletRequest req)
    {
        String responseType = req.getParameter("responseType");
        if(StringUtils.isEmpty(responseType))
        {
            responseType = "application/json";
        }
        return responseType;
    }
    
    protected Object objectByType(String objectName)
    {
        return typeFactory.objectByType(objectName);
    }

    protected String encode(Object reply)
    {
    	if(reply instanceof StringReply)
    	{
    		return ((StringReply) reply).string();
    	}
        try
		{
			return codec.encode(reply);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }
}