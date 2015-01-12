package xingu.servlet.container.impl.jetty;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.factory.Factory;

@Deprecated
public class ServletContext
    implements HandlerFactory, Configurable
{
    @Inject
    private Factory factory;
    
    private String contextPath;
    
    private List<ServletMapping> servlets = new ArrayList<ServletMapping>();
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        contextPath = conf.getAttribute("context");
        for(Configuration servletConf : conf.getChildren("servlet"))
        {
            ServletMapping servlet = factory.create(ServletMapping.class, servletConf);
            servlets.add(servlet);
        }
    }

    @Override
    public Handler createHandler()
    {
        logger.info("Creating servlet context '{}'", contextPath);
        ServletHandler servletHandler = new ServletHandler();
        for(ServletMapping mapping : servlets)
        {
            logger.info("Adding servlet to context '{}': {}", new Object[]{contextPath, mapping});
            mapping.addServletTo(servletHandler);
        }
        ContextHandler context = new ContextHandler(contextPath);
        context.setHandler(servletHandler);
        return context;
    }
}
