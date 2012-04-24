package xingu.servlet.container.impl.jetty;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.utils.ObjectUtils;

public class HTTP
    implements ServerConfig
{
    private int port;

    private String connectorClass;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String host;

    @Override
    public void configure(Configuration conf)
    throws ConfigurationException
    {
        conf = conf.getChild("server").getChild("http");
        port = conf.getAttributeAsInteger("port", 80);
        host = conf.getAttribute("host", null);
        connectorClass = conf.getAttribute("connector", SocketConnector.class.getName());
    }
    
    @Override
    public void applyTo(Server server)
    {
        Connector connector = (Connector) ObjectUtils.getInstance(connectorClass);
        connector.setPort(port);
        connector.setHost(host);
        server.addConnector(connector);
        logger.info("HTTP is enabled on port {} host '{}' connector '{}'", new Object[]{port, host, connectorClass});
    }
}
