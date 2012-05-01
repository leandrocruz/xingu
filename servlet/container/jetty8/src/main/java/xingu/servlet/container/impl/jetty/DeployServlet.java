package xingu.servlet.container.impl.jetty;

import javax.servlet.Servlet;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class DeployServlet
    implements HandlerFactory, Configurable
{
    @Inject
    private Factory factory;
    
    @Inject
    private SessionBroker sessionBroker;
    
    private String contextPath;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private Configuration[] servlets;
    
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        contextPath = conf.getAttribute("context");
        servlets = conf.getChildren("servlet"); 
    }

    @Override
    public Handler createHandler()
        throws Exception
    {
        logger.info("deploying servlet '{}'", contextPath);
        ServletHandler servletHandler = new ServletHandler();
        for(Configuration mapping : servlets)
        {
            String className = mapping.getAttribute("class");
            String path = mapping.getAttribute("path");
            logger.info("Adding servlet '{}' on {}{}", new Object[]{className, contextPath, path});

            Servlet servlet = (Servlet) factory.create(className /*, servletConf */ );
            ServletHolder holder = new ServletHolder(servlet);
            servletHandler.addServletWithMapping(holder, path);
        }
        SessionHandler sessionHandler = sessionBroker.handlerFor(null);
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath(contextPath);
        ctx.setSessionHandler(sessionHandler);
        ctx.setHandler(servletHandler);
        return ctx;
    }

    private static void printStackTrace(StackTraceElement[] stackTrace) {
        for(StackTraceElement e : stackTrace) {
            System.out.println(e.toString());
        }
    }
}
