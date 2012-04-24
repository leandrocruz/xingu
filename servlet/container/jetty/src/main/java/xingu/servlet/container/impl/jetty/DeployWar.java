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

public class DeployWar
    implements HandlerFactory, Configurable
{
    private String contextPath;
    
    private String warFile;

    private String displayName;

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

        logger.info("deploying webapp '{}' from war file '{}'", contextPath, warFile);
        //SessionHandler sessionHandler = sessionBroker.handlerFor(null);
        
        /*
         * see WebAppDeployer.scan()
         */
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath(contextPath);
        ctx.setExtractWAR(true);
        ctx.setWar(warFile);
        ctx.setParentLoaderPriority(useParentClassLoader);
        ctx.setLogUrlOnStart(true);
        auth.applyTo(ctx);
        return ctx;
    }
}