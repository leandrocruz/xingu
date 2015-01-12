package xingu.servlet.container;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import xingu.container.Container;
import xingu.container.ContainerUtils;

public class InjectedServlet
    extends HttpServlet
{
    @Override
    public void init(ServletConfig config) 
        throws ServletException
    {
        try
        {
            getContainer().startLifecycle(this, null);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new ServletException("Error initializing servlet", e);
        }
        super.init(config);
    }

    protected Container getContainer() 
        throws Exception
    {
        return ContainerUtils.getContainer();
    }
}
