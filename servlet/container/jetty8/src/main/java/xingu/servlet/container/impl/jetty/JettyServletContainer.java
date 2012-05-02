package xingu.servlet.container.impl.jetty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import xingu.servlet.container.ApplicationContext;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.servlet.container.ServletContainer;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class JettyServletContainer
    implements ServletContainer, Configurable, Startable
{
    @Inject
    private Factory factory;
    
    private Server server;
    
    private List<ServerConfig> configurations = new ArrayList<ServerConfig>();
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        configurations.add(factory.create(HTTP.class, conf));
        configurations.add(factory.create(SSL.class, conf));
        configurations.add(factory.create(ContextHandlers.class, conf));
    }
    
    @Override
    public void start()
        throws Exception
    {
        logger.info("Starting Jetty");
        server = new Server();
        for(ServerConfig config : configurations)
        {
            config.applyTo(server);
        }
        server.start();
        for(Connector connector : server.getConnectors())
        {
            logger.info("Jetty connector ready: {}", connector);
        }
    }
    
    @Override
    public void stop()
        throws Exception
    {
        logger.info("Stopping Jetty");
        if(server != null)
        {
            server.stop();
        }
    }
    
    @Override
    public ApplicationContext context(String name)
    {
        for(ApplicationContext context : contexts())
        {
            if(name.equals(context.name()))
            {
                return context;
            }
        }
        return null;
    }

    @Override
    public Collection<ApplicationContext> contexts()
    {
        List<ApplicationContext> list = new ArrayList<ApplicationContext>();
        for(Handler handler : server.getChildHandlers())
        {
            if(handler instanceof WebAppContext)
            {
                list.add(new JettyApplicationContext((WebAppContext) handler));
            }
        }
        return list;
    }
}