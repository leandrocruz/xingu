package xingu.servlet.container.impl.jetty;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthConfig
{
    private boolean enabled;
    
    private String realmName;
    
    private String realmConfig;

    private String authMethod;
    
    private Configuration[] constraints;

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public AuthConfig(Configuration conf)
        throws ConfigurationException
    {
        enabled = conf != null;
        if(enabled)
        {
            realmConfig = conf.getAttribute("realmConfig");
            realmName = conf.getAttribute("realmName");
            authMethod = conf.getAttribute("method");
            constraints = conf.getChildren("constraint");
        }
    }

    public void applyTo(ServletContextHandler context)
        throws Exception
    {
        if(!enabled)
        {
            return;
        }
        File file = new File(realmConfig);
        if(!file.exists())
        {
            logger.warn("Auth disabled for '{}': realm config not found at '{}'", context.getContextPath(), realmConfig);
            return;
        }
        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setAuthMethod(authMethod);
        securityHandler.setRealmName(realmName);
        securityHandler.setLoginService(new HashLoginService(realmName, realmConfig));
        securityHandler.setConstraintMappings(createConstraintMappings(context.getContextPath()));

        context.setSecurityHandler(securityHandler);
    }

    private ConstraintMapping[] createConstraintMappings(String contextPath)
        throws Exception
    {
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
