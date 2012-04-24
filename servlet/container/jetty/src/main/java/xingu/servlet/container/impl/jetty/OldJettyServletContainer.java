package xingu.servlet.container.impl.jetty;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import xingu.servlet.container.ApplicationContext;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.servlet.container.ServletContainer;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.io.FileUtils;

/**
 * To create user passwords, see http://docs.codehaus.org/display/JETTY/Securing+Passwords
 */
public class OldJettyServletContainer
	implements ServletContainer, Configurable, Startable
{
    @Inject
    private Factory factory;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String host;
    
    private int port;
	
    private WebApps webApps; 
	
	private Collection<Mapping> servlets = new ArrayList<Mapping>();
	
	private Server server;
    
	private SSLConfig ssl;
	
	private Map<String, ApplicationContext> map = new HashMap<String, ApplicationContext>();
	
	@Override
	public void configure(Configuration conf) 
	    throws ConfigurationException
	{
	    conf = conf.getChild("server");
	    this.port = conf.getAttributeAsInteger("port", 8080);
	    this.host = conf.getAttribute("host", null);
	    configureSSL(conf.getChild("ssl", false));
	    webApps = new WebApps(conf.getChildren("webapp"));
	    configureServlets(conf.getChild("servlets"));
	}

	private void configureSSL(Configuration conf)
        throws ConfigurationException
    {
	    ssl = new SSLConfig();
	    if(conf != null)
	    {
	        ssl.enabled = true;
	        ssl.port = conf.getAttributeAsInteger("port", 443);
	        ssl.keyStore = conf.getChild("store").getAttribute("path");
	        ssl.keyStorePassword = conf.getChild("store").getAttribute("password");
	        logger.info("SSL port is {}", ssl.port);
	    }
    }
	    
	private void configureServlets(Configuration conf)
            throws ConfigurationException
    {
        for (Configuration servlet : conf.getChildren("servlet"))
        {
            String path = servlet.getAttribute("path");
            String className = servlet.getAttribute("class");
            servlets.add(new Mapping(className, path));
        }
    }
	
	@Override
	public void stop()
	    throws Exception
	{
        if (server != null)
        {
            server.stop();
        }
	}
	
	@Override
	public void start()
	    throws Exception
	{
		if(port > 0)
		{
		    server = new Server(port);
		}
		else
		{
		    server = new Server();
		}

		if(host != null)
		{
		    server.getConnectors()[0].setHost(host);
		}
		
		addSSL();
		webApps.create();
		addServlets();

		server.start();
		logger.info("Jetty Servlet Container ready on port '{}'", port);
	}

    private void addSSL()
    {
        if(ssl.enabled)
        {
            File keyStore = new File(ssl.keyStore);
            if(!keyStore.exists())
            {
                logger.warn("SSL is disabled. Key store '{}' was not found ", keyStore);
            }
            else
            {
                SslSocketConnector sslConnector = new SslSocketConnector();
                sslConnector.setPort(ssl.port);
                sslConnector.setKeystore(ssl.keyStore);
                sslConnector.setKeyPassword(ssl.keyStorePassword);
                server.addConnector(sslConnector);
            }
        }
    }

    private void addServlets()
    {
        if(servlets.size() > 0)
		{
		    ServletHandler handler = new ServletHandler();
		    server.addHandler(handler);
		    for(Mapping info : servlets)
		    {
		        Servlet servlet = (Servlet) factory.create(info.className);
		        ServletHolder holder = new ServletHolder(servlet);
		        handler.addServletWithMapping(holder, info.path);
		    }
		}
    }
    
    private class WebApps
    {
		private Configuration[] array;

		public WebApps(Configuration[] config)
		{
			this.array = config;
		}

		public void create()
			throws Exception
		{
	        for (Configuration conf : array)
	        {
	        	String warFile = conf.getAttribute("war");
	        	warFile = FileUtils.findRealName(warFile);
	        	File file = new File(warFile);
	        	if(!file.exists())
	        	{
	        	    logger.warn("webapp not found: {}", warFile);
	        	    continue;
	        	}
	        	WebAppContext ctx = createContext(conf);
	        	String path = ctx.getContextPath();
	        	logger.info("adding webapp '{}' to contextpath '{}'", ctx.getWar(), path);
	            map.put(path, new JettyApplicationContext(ctx));
	            server.addHandler(ctx);
	        }
		}
		
		private WebAppContext createContext(Configuration conf)
		    throws Exception
		{
            String warFile = conf.getAttribute("war");
            warFile = FileUtils.findRealName(warFile);
            String contextPath = conf.getAttribute("context", "/");
		    WebAppContext result = new WebAppContext(warFile, contextPath);
		    
            result.setDisplayName(conf.getAttribute("displayName"));
            result.setLogUrlOnStart(true);
            
            String[] hosts = virtualHosts(conf);
            if(hosts != null && hosts.length > 0)
            {
                result.setVirtualHosts(hosts);
            }
            Configuration authConfig = conf.getChild("auth", false);
            if(authConfig != null)
            {
                configureAuthentication(result, authConfig);
            }
            //this forces jetty to use parent class loader's classes first, so that
            //both webapp and broker use the same pulga singleton container.
            boolean useParentClassLoader = conf.getAttributeAsBoolean("useParentClassLoader", true);
            result.setParentLoaderPriority(useParentClassLoader);
            return result;
        }

        String[] virtualHosts(Configuration webAppConfig)
		    throws Exception
		{
            Configuration[] hostsConfig = webAppConfig.getChild("virtualHosts").getChildren("host");
            if(hostsConfig.length <= 0)
            {
                return null;
            }
            String[] hosts = new String[hostsConfig.length];
            int i = 0;
            for (Configuration hostConfig : hostsConfig)
            {
                hosts[i++] = hostConfig.getAttribute("name");
            }
            return hosts;
		}


        private void configureAuthentication(WebAppContext context, Configuration authConfig)
			throws Exception
		{
			String realmConfig = authConfig.getAttribute("realmConfig");
			String realmName = authConfig.getAttribute("realmName");
			HashUserRealm realm = new HashUserRealm(realmName);
			File configFile = new File(realmConfig);
            if(!configFile.exists())
			{
			    logger.warn("Realm config file not found for context '{}': {}", context.getContextPath(), realmConfig);
			    return;
			}
			realm.setConfig(realmConfig);
			String authMethod = authConfig.getAttribute("method");
			ConstraintMapping[] mappings = createConstraintMappings(context.getContextPath(), authConfig);
			SecurityHandler securityHandler = context.getSecurityHandler();
			securityHandler.setAuthMethod(authMethod);
			securityHandler.setUserRealm(realm);
			securityHandler.setConstraintMappings(mappings);
		}

		private ConstraintMapping[] createConstraintMappings(String contextPath, Configuration authConfig)
			throws Exception
		{
			Configuration[] constraints = authConfig.getChildren("constraint");
			ConstraintMapping[] mappings = new ConstraintMapping[constraints.length];
			for(int index=0; index<constraints.length; index++)
			{
				Configuration cfg = constraints[index];
				
				String[] roles = cfg.getAttribute("roles").split(" *, *");
				boolean authenticate = cfg.getAttributeAsBoolean("authenticate", true);
				Constraint constraint = new Constraint();
				constraint.setRoles(roles);
				constraint.setAuthenticate(authenticate);
				
				ConstraintMapping mapping = new ConstraintMapping();
				String pathSpec = contextPath + cfg.getAttribute("path");
				mapping.setPathSpec(pathSpec);
				mapping.setConstraint(constraint);
				
				mappings[index] = mapping;
			}
			return mappings;
		}
    }

    @Override
    public ApplicationContext context(String name)
    {
        return map.get(name);
    }

    @Override
    public Collection<ApplicationContext> contexts()
    {
        return map.values();
    }
}

class Mapping
{
    String className;
    
    String path;

    public Mapping(String className, String path)
    {
        this.className = className;
        this.path = path;
    }
}

class SSLConfig
{
    boolean enabled;

    int port;
    
    String keyStore;

    String keyStorePassword;
}