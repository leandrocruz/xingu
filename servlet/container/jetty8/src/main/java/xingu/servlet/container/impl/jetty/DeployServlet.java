package xingu.servlet.container.impl.jetty;

import javax.servlet.Servlet;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.ObjectUtils;

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
        logger.info("Deploying servlet '{}'", contextPath);

        SessionHandler sessionHandler = sessionBroker.handlerFor(null);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(contextPath);
        context.setSessionHandler(sessionHandler);

        for(Configuration mapping : servlets)
        {
            String className = mapping.getAttribute("class");
            String path = mapping.getAttribute("path");
            logger.info("Adding servlet '{}' on {}{}", new Object[]{className, contextPath, path});

            Class<?>      clazz   = ObjectUtils.loadClass(className);
            Servlet       servlet = (Servlet) factory.create(clazz /*, servletConf */ );
            ServletHolder holder  = new ServletHolder(servlet);
            context.addServlet(holder, path);
        }

        return context;
    }

}
