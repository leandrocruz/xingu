package xingu.servlet.container.impl.jetty;

import java.io.File;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.utils.io.FileUtils;

@Deprecated
public class War
    implements HandlerFactory, Configurable
{
    private String contextPath;
    
    private String warFile;

    private String displayName;

    private VirtualHosts virtualHosts;
    
    private AuthConfig auth;
    
    private boolean useParentClassLoader;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        contextPath = conf.getAttribute("context");
        warFile = FileUtils.findRealName(conf.getAttribute("war"));
        displayName = conf.getAttribute("displayName");
        useParentClassLoader = conf.getAttributeAsBoolean("useParentClassLoader", true);
        virtualHosts = new VirtualHosts(conf.getChild("virtualHosts", false));
        auth = new AuthConfig(conf.getChild("auth", false));
    }

    @Override
    public Handler createHandler()
        throws Exception
    {
        File file = new File(warFile);
        if(!file.exists())
        {
            logger.error("Error loading webapp '{}', war file not found '{}'", new Object[]{displayName, warFile});
            return null;
        }
        logger.info("Loading webapp '{}' context '{}': {}", new Object[]{displayName, contextPath, warFile});
        WebAppContext context = new WebAppContext();
        context.setContextPath(contextPath);
        context.setWar(warFile);
        context.setDisplayName(displayName);
        context.setParentLoaderPriority(useParentClassLoader); //forces jetty to use parent's class loader so both webapp and broker share same xingu container
        context.setLogUrlOnStart(true);
        virtualHosts.addTo(context);
        auth.applyTo(context);
        return context;
    }
}
