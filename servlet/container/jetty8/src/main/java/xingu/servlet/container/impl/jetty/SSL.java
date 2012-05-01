package xingu.servlet.container.impl.jetty;

import java.io.File;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSL
    implements ServerConfig
{
    private boolean enabled;
    
    private int port;

    private String keyStore;

    private String keyStorePassword;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        conf = conf.getChild("server").getChild("ssl", false);
        enabled = conf != null;
        if(enabled)
        {
            port = conf.getAttributeAsInteger("port", 443);
            keyStore = conf.getChild("store").getAttribute("path");
            keyStorePassword = conf.getChild("store").getAttribute("password");
        }
    }
    
    @Override
    public void applyTo(Server server)
    {
        if(!enabled)
        {
            logger.info("SSL is disabled");
            return;
        }
        File keyStoreFile = new File(keyStore);
        if(keyStoreFile.exists())
        {
            SslSocketConnector sslConnector = new SslSocketConnector();
            sslConnector.setPort(port);
            sslConnector.setKeystore(keyStore);
            sslConnector.setKeyPassword(keyStorePassword);
            server.addConnector(sslConnector);
            logger.info("SSL is enabled on port {}", port);
        }
        else
        {
            logger.warn("SSL is disabled: key store not found at '{}'", keyStore);
        }
    }
}
